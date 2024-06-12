package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.domain.Role;
import ypjs.project.domain.Status;
import ypjs.project.service.MemberService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class LoginApiController {
    private final MemberService memberService;

    // 로그인
    @PostMapping("/ypjs/member/login")
    public String login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        Long memberId = memberService.login(loginForm.getAccountId(),loginForm.getPassword());
        Member member = memberService.findOne(memberId);
        ResponseLogin responseLogin = new ResponseLogin(member.getMemberId(),member.getAccountId(),member.getPassword(),member.getNickname(),member.getGender(),member.getPoint(),
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


    // 로그인 응답 DTO
    @Data
    @AllArgsConstructor
    public static class ResponseLogin {
        private Long memberId;
        private String accountId;
        private String password;
        private String nickname;
        private String gender;
        private int point;
        private String name;
        private String email;
        private Address address;
        private String phonenumber;
        private LocalDateTime joinDate;
        private Role role;
        private Status status;
    }
}
