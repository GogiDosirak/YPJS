package ypjs.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 멤버 가입
    @GetMapping("/ypjs/member/join")
    public String join() {
        return "member/join";
    }

}
