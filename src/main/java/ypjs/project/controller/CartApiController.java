package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.cartdto.CartAddDto;
import ypjs.project.dto.cartdto.CartListDto;
import ypjs.project.dto.cartdto.CartUpdateDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.CartService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/cart")
public class CartApiController {

    private final CartService cartService;

    //==장바구니 추가==//
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid CartAddDto cartAddDto, HttpServletRequest request) {
        System.out.println("**장바구니 추가 요청됨");

        HttpSession session = request.getSession();
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");
        Long memberId = loginMember.getMemberId();

        for(Long itemId : cartService.findItemIdByMemberId(memberId)) {
            if(cartAddDto.getItemId().equals(itemId)) {
                return ResponseEntity.badRequest().body("장바구니에 이미 추가된 상품입니다.");
            }
        }

        cartAddDto.setMemberId(memberId);
        cartService.add(cartAddDto);

        return ResponseEntity.ok().build();
    }

    //==장바구니 수량 변경==//
    @PostMapping("/update")
    public String update(@RequestBody @Valid CartUpdateDto cartUpdateDto) {
        cartService.update(cartUpdateDto);
        return "redirect:/ypjs/cart/list";
    }

    //==장바구니 삭제==//
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam("cartId") @Valid Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build(); // Respond with 204 No Content
    }

    //==장바구니 상품 주문하기==//
    @PostMapping("/order")
    public ResponseEntity<Void> order(@RequestBody @Valid List<CartListDto> cartListDtos, HttpServletRequest request) {
        System.out.println("**장바구니 상품 주문 요청됨");
        System.out.println(cartListDtos);

        request.getSession().setAttribute("cartList", cartListDtos);

        return ResponseEntity.ok().build();
    }


}
