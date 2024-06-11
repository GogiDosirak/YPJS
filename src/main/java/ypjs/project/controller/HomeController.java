package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ypjs.project.domain.Member;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "/";
    }


}
