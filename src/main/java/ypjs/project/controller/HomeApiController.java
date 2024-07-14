package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeApiController {
    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/index")
    public String home() {
        return "/index";
    }


}
