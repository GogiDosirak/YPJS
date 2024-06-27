package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ypjs.project.common.auth.JwtToken;
import ypjs.project.common.auth.SecurityUtil;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.logindto.LoginForm;
import ypjs.project.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;

//    // 로그인
//    @PostMapping("ypjs/member/login")
//    public String login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
//        Long memberId = memberService.login(loginForm.getAccountId(),loginForm.getPassword());
//        Member member = memberService.attendancePoint(memberId);
//        LoginDto.ResponseLogin responseLogin = new LoginDto.ResponseLogin(member.getMemberId(),member.getAccountId(),member.getNickname());
//        HttpSession session = request.getSession();
//        session.setAttribute("member", responseLogin);
//        System.out.println("로그인 성공");
//        return "redirect:/";
//    }


    @PostMapping("ypjs/member/login")
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
