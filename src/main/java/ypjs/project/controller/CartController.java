package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ypjs.project.dto.CartAddDto;
import ypjs.project.dto.CartUpdateDto;
import ypjs.project.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/cart")
public class CartController {

    private final CartService cartService;

    //==장바구니 추가==//
    @PostMapping("/add")
    public void add(@RequestBody @Valid CartAddDto cartAddDto) {
        cartService.add(cartAddDto);
    }

    //==장바구니 수량 변경==//
    @PostMapping("/update")
    public void update(@RequestBody @Valid CartUpdateDto cartUpdateDto) {
        cartService.update(cartUpdateDto);
    }

    //==멤버별 장바구니 전체 조회==//
    public void findAllWithMemberId(@PathVariable @Valid Long memberId) {
        cartService.findAllWithMemberId(memberId);
    }

    //==장바구니 삭제==//
    @PostMapping("/delete/{cartId}")
    public void delete(@PathVariable @Valid Long cartId) {
        cartService.delete(cartId);
    }
}
