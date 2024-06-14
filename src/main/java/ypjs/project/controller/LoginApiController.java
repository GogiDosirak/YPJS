package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.logindto.LoginForm;
import ypjs.project.service.MemberService;

@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;

    // 로그인
    @PostMapping("/ypjs/member/login")
    public String login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        Long memberId = memberService.login(loginForm.getAccountId(),loginForm.getPassword());
        Member member = memberService.attendancePoint(memberId);
        LoginDto.ResponseLogin responseLogin = new LoginDto.ResponseLogin(member.getMemberId(),member.getAccountId(),member.getPassword(),member.getNickname(),member.getGender(),member.getPoint(),
                member.getName(),member.getEmail(),member.getAddress(),member.getPhonenumber(),member.getJoinDate(),member.getRole(),member.getStatus());
        HttpSession session = request.getSession();
        session.setAttribute("member", responseLogin);
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/ypjs/member/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


}
