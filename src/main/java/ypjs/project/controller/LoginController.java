package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {


    @GetMapping("ypjs/member/login")
    public String login() {
        return "member/login";
    }



    // 로그아웃
    @GetMapping("ypjs/member/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        System.out.println("로그아웃 처리됨");
        return "redirect:/";
    }

}
