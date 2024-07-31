package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/cart")
public class CartViewController {

    private final CartService cartService;

    //==멤버별 장바구니 전체 조회==//
    @GetMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        //멤버정보
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");

        if(loginMember == null) {
            request.setAttribute("msg", "로그인이 필요합니다.");
            request.setAttribute("url", "/ypjs/member/login");
            return "alert";
        }

        Long memberId = loginMember.getMemberId();

        model.addAttribute("cartList", cartService.findAllByMemberId(memberId));
        return "cart/list";
    }


}
