package ypjs.project.common.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ypjs.project.domain.Member;
import ypjs.project.domain.enums.Role;
import ypjs.project.dto.memberdto.CustomUserDetails;

import java.io.IOException;
import java.io.PrintWriter;

// 요청에 대해 한번만 응답하는 필터 상속
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /* 다중 토큰의 경우 */
        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request,response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response Body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired"); // 콘솔에 응답

            //response status code, 상태 코드는 프론트엔드와 협의된 코드를 넘겨야함. 400달라면 400
            //why? 프론트측에서 다시 만료된 토큰을 갖고 리프레쉬 토큰을 줘서 재발급 받을 수 있도록
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // 중요한건, 그 다음 필터로 넘기면 안되므로 doFilter 하지말고 바로 만료가 되었다고 응답을 return

        }

        // 토큰이 access인지 확인 (발급시 페이로드에 카테고리로 명시해줬음)
        String category = jwtUtil.getCategory(accessToken);

        if(!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; //그 다음 필터로 넘기면 안되므로 doFilter 하지말고 바로 access가 아니라고 응답을 return

        }

        // 토큰 검증이 완료되었으므로
        // 토큰 내부에서 회원 정보 값을 획득 -> 일시적 세션 생성
        String username = jwtUtil.getUsername(accessToken);
        String roleStr = jwtUtil.getRole(accessToken);

        // Role 타입으로 변경
        Role role = Role.valueOf(roleStr);

        Member member = new Member();
        member.setUsername(username);
        member.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 검증 되었다면 일시적 세션 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 넘겨주기
        filterChain.doFilter(request,response);

    }
}


