package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ypjs.project.common.auth.JwtToken;
import ypjs.project.common.auth.SecurityUtil;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.logindto.LoginForm;
import ypjs.project.service.MemberService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    

    @PostMapping("/api/ypjs/member/login")
    public JwtToken login(@RequestBody LoginDto.RequestLogin requestLogin) {
        String accountId = requestLogin.getAccountId();
        String password = requestLogin.getPassword();
        JwtToken jwtToken = memberService.login(accountId, password);
        log.info("request accountId = {}, password = {}", accountId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("ypjs/member/test")
    public String test() {
        return SecurityUtil.getCurrentAccountId();
    }




}
