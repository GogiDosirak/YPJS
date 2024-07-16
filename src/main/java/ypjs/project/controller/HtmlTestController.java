package ypjs.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlTestController {

    @GetMapping("/shop")
    public String shop() {
        return "shop";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/cart")
    public String cart() {
        return "/cart/cartList";
    }
}
