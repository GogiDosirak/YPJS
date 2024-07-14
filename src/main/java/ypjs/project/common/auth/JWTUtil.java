package ypjs.project.common.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ypjs.project.dto.memberdto.CustomUserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component //Component 클래스로 관리
public class JWTUtil {
    // 객체 키를 저장할 공간
    private SecretKey secretKey;

    // 클래스가 호출될 때 미리 저장해둔 Secret Key 불러오기
    // @Value : application.yml에서의 특정한 변수 데이터를 가져올 수 있음
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        // String 타입으로 받은 키를 객체타입으로 만들어 저장
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 검증 메소드 세가지 - username, role값 꺼내기 + 토큰 만료확인
    // 각각의 메소드는 토큰을 전달받아 Jwts.parser를 통해 내부 데이터를 확인하고 꺼내줌
    public String getUsername(String token) {
        // 암호화가 된 정보를 secretKey를 갖고 검증 -> 토큰이 우리 서버에서 생성된건지
        // 검증이 완료되면 토큰을 파싱하여 클레임을 추출 -> 클레임에서 username이라는 키값을 String으로 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role",String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 토큰 생성 메소드
    // 로그인이 완료됐을 때, successful Handler를 통해 유저정보, 만료시간을 전달받아
    // 토큰을 생성해 반환
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);

    }

    public String createJwt(String category, String username, String role, Long expiredMs) { // 토큰이 어느정도 살아있을지도 전달

        return Jwts.builder()
                .claim("category",category) //JWT 내부에 payload 부분에 클레임으로 회원 정보 넣어줌
                .claim("username",username)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis())) // 발행시간으로 현재 시간을 넣어줌
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료시간도 넣어줌
                .signWith(secretKey) //secretKey로 암호화 진행
                .compact();
    }




}
