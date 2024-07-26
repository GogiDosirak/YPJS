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

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.logindto.LoginForm;

import ypjs.project.service.CartService;


import ypjs.project.service.MemberService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;



    private final CartService cartService;
  


    // 세션버전 로그인
    @PostMapping("/api/ypjs/member/login")
    public String login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        Long memberId = memberService.login(loginForm.getUsername(), loginForm.getPassword());
        Member member = memberService.findOne(memberId);
        memberService.attendancePoint(member.getMemberId());
        LoginDto.ResponseLogin responseLogin = new LoginDto.ResponseLogin(member.getMemberId(),member.getUsername(), member.getNickname(), member.getRole());
        HttpSession session = request.getSession();
        session.setAttribute("member", responseLogin);
        session.setAttribute("memberCartSize", cartService.findAllByMemberId(responseLogin.getMemberId()).size());
        return "redirect:/";
    }

//    @PostMapping("/api/ypjs/member/login")
//    public String login(@RequestBody LoginForm loginForm) {
//        // 로그인 처리 로직
//        return "Login successful";
//    }

}



