package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/cart")
public class CartViewController {

    private final CartService cartService;

    //==멤버별 장바구니 전체 조회==//
    @GetMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        //HttpSession session = request.getSession();
        //멤버정보
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        Long memberId = 1L;  //임시

        if(memberId == null) {
            return "redirect:/ypjs/member/login";
        }

        model.addAttribute("cartList", cartService.findAllByMemberId(memberId));
        return "cart/list";
    }


}
