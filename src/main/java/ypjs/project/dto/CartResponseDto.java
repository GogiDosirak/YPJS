package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ypjs.project.domain.Cart;

@Getter
public class CartResponseDto {

    private Long itemId;  //상품번호

    private int itemCount;  //상품수량

    public CartResponseDto(Cart cart) {
        itemId = cart.getItem().getItemId();
        itemCount = cart.getItemCount();
    }
}
