package ypjs.project.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ypjs.project.domain.RefreshEntity;
import ypjs.project.dto.memberdto.CustomUserDetails;
import ypjs.project.repository.RefreshRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //해당 정보를 토큰에 담아 authenticationManager에게 전달
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null); // 세번째값은 role값

        return authenticationManager.authenticate(authToken); // 이쪽으로 던져주면, 자동으로 검증 진행
    }


    // 로그인 검증 성공시 실행하는 메소드 (여기서 JWT 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //authentication에 저장된 유저객체를 customDetails로 가져옴
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // authentication 객체에서 아이디와 롤값을 빼냄
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 뽑아낸 값으로 JWTUtil에 토큰을 만들어달라고 값을 전달 -> JWT 생성
        String access = jwtUtil.createJwt("access",username, role,600000L); // 10분의 생명주기
        String refresh = jwtUtil.createJwt("refresh",username,role,86400000L); // 24시간의 생명주기


        // JWT를 response의 헤더부분에 담아서 응답
        response.setHeader("access",access);  // 응답 헤더에 access토큰을 access키에 넣어줌
        response.addCookie(createCookie("refresh",refresh)); // 응답 쿠키에 refresh 토큰을 넣어줌
        response.setStatus(HttpStatus.OK.value()); // 200응답을 할 수 있도록 상태코드도 설정

        addRefreshEntity(username, refresh, 86400000L);

    }

    // 쿠키 생성 메소드
    private Cookie createCookie(String key, String value) { // 키값과 JWT가 들어갈 value를 인자로 받음
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 쿠키의 생명주기는 Refresh와 동일하게
        //cookie.setSecure(true); Https 통신을 진행할 경우 넣어주기
        //cookie.setPath("/"); 쿠키가 적용될 범위
        cookie.setHttpOnly(true); // 클라이언트단에서 js로 해당 쿠키를 접근할 수 없도록 막아줌

        return cookie;

    }

    // refresh 토큰을 저장소에 저장하는 메소드
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }

//    @Override
//    public void setFilterProcessesUrl(String filterProcessesUrl) {
//        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/ypjs/member/login"));
//    }
}
