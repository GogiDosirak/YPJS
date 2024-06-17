package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.cartdto.CartAddDto;
import ypjs.project.dto.cartdto.CartResponseDto;
import ypjs.project.dto.cartdto.CartUpdateDto;
import ypjs.project.dto.deliverydto.DeliveryDto;
import ypjs.project.dto.orderdto.OrderItemDto;
import ypjs.project.service.CartService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/cart")
public class CartController {

    private final CartService cartService;

    //==멤버별 장바구니 전체 조회==//
    @GetMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        //HttpSession session = request.getSession();
        //멤버정보
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        Long memberId = 1L;  //임시

        if(memberId == null) {
            return "redirect:/member/login";
        }

        model.addAttribute("cartList", cartService.findAllByMemberId(memberId));
        return "cart/list";
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
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam("cartId") @Valid Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build(); // Respond with 204 No Content
    }

    //==장바구니 상품 주문하기==//
    @PostMapping("/order")
    public ResponseEntity<Void> order(@RequestBody @Valid List<OrderItemDto> orderItemDtos, Model model, HttpSession session, HttpServletRequest request) {

        System.out.println(orderItemDtos);

        request.setAttribute("orderItemList", orderItemDtos);

        model.addAttribute("orderItemList", orderItemDtos);

        request.getSession().setAttribute("orderItemList", orderItemDtos);

        return ResponseEntity.ok().build();
    }


}
