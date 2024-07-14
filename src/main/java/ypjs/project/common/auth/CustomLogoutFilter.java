package ypjs.project.common.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;
import ypjs.project.repository.RefreshRepository;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    // 의존성 주입
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HttpServlet으로 해당 request와 response를 casting해서 우리가 직접 커스텀할 doFilter 메소드 호출
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 필터기 때문에, 모든 요청이 다 지나감
        // 그 중, 로그아웃 요청만 받아야함!
        // URI의 파라미터(path)값을 꺼내서 문자열에 담고, logout 경로인지 확인
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            // 아니면 다음 필터로
            filterChain.doFilter(request, response);
            return;
        }
        // 로그아웃이어도 post 요청이 아니면 다음필터로 넘김
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 refresh 토큰을 꺼내오기 위해 쿠키를 다 불러옴
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        // 해당 쿠키에 refresh토큰이 없다면 BAD REQUEST(400) 응답
        if (refresh == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 있다면 만료가 되었는지 확인, 만료가 됐다면 로그아웃이 된거기떄문에 추가로 작업X
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거 - 제거해야 Reissue를 못함
        refreshRepository.deleteByRefresh(refresh);

        //쿠키에 세팅된 Refresh키에 저장된 refresh 토큰을 null값으로 바꿔줌
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        //응답에 해당 쿠키를 넣어주고 200코드 응답
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
