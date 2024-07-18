package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.logindto.LoginForm;
import ypjs.project.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;

    // 세션버전 로그인
    @PostMapping("/api/ypjs/member/login")
    public String login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        Long memberId = memberService.login(loginForm.getUsername(), loginForm.getPassword());
        Member member = memberService.findOne(memberId);
        LoginDto.ResponseLogin responseLogin = new LoginDto.ResponseLogin(member.getMemberId(),member.getUsername(), member.getNickname());
        HttpSession session = request.getSession();
        session.setAttribute("member", responseLogin);
        return "redirect:/";
    }







}