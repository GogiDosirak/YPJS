package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.CartAddDto;
import ypjs.project.dto.CartResponseDto;
import ypjs.project.dto.CartUpdateDto;
import ypjs.project.dto.OrderItemDto;
import ypjs.project.service.CartService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/cart")
public class CartController {

    private final CartService cartService;

    //==멤버별 장바구니 전체 조회==//
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        //멤버정보
        Long memberId = (Long) session.getAttribute("loginMemberId");

        if(memberId == null) {
            return "redirect:/member/login";
        }

        model.addAttribute("cartList", cartService.findAllByMemberId(memberId));
        return "cartList";
    }

    //==장바구니 추가==//
    @PostMapping("/add")
    public void add(@RequestBody @Valid CartAddDto cartAddDto) {
        cartService.add(cartAddDto);
    }

    //==장바구니 수량 변경==//
    @PostMapping("/update")
    public String update(@RequestBody @Valid CartUpdateDto cartUpdateDto) {
        cartService.update(cartUpdateDto);
        return "redirect:/cart/list";
    }

    //==장바구니 삭제==//
    @DeleteMapping("/delete/{cartId}")
    public String delete(@PathVariable @Valid Long cartId) {
        cartService.delete(cartId);
        return "redirect:/cart/list";
    }

    //==장바구니 상품 주문하기==//
    @PostMapping("/order")
    public String order(@RequestBody @Valid List<CartResponseDto> cartDtos, Model model) {

        List<OrderItemDto> orderItemDtos = cartService.createOrderItems(cartDtos);

        model.addAttribute("orderItemDtos", orderItemDtos);

        return "redirect:/order/create";
    }
}
