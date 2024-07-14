package ypjs.project.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.common.auth.JWTUtil;
import ypjs.project.domain.RefreshEntity;
import ypjs.project.repository.RefreshRepository;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    // JWT를 받아서 검증하고 새로운 JWT를 응답해줘야하기 때문에
    // JWT를 관리하고 검증하는 JWTUtil 클래스를 주입
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) { // 앞단에서의 요청을 받아야하기 때문에 HttpServletRequest

        // refresh 토큰을 request의 쿠키에서 뽑아내야함
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        // 쿠기가 없을 경우
        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 있을 경우 만료됐는지 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 만료까지 안됐을 경우, refresh 토큰인지 확인 (발급시 페이로드의 category 명시해줬음)
        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")) {

            //response status code
            return  new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            // response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 여기까지 오면 모든 검증이 끝났으므로,
        // 토큰의 정보를 저장해서 access 토큰을 만들어줌
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access",username,role,600000L);
        // Rotate, Access 토큰을 만들 때 새로운 Refresh 토큰도 생성
        String newRefresh = jwtUtil.createJwt("refresh",username,role, 86400000L);

        // DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username,newRefresh,86400000L);

        // 응답 헤더에 access 토큰키에 새로운 엑세스 토큰을 넣고,
        // 쿠키에 새로운 refresh 토큰을 쿠키를 생성해서 추가
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh",newRefresh));

        return new ResponseEntity<>(HttpStatus.OK); // 200코드와 함꼐 return

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


    // 쿠키 생성 메소드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}