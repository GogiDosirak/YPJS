package ypjs.project.dto.cartdto;

import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Cart;

@Data
public class CartResponseDto {

    private Long cartId; //장바구니번호

    private Long itemId;  //상품번호

    private String itemName;  //상품명

    private String itemContent;  //상품설명

    private int itemPrice;  //상품가격

    private String itemFilepath;  //이미지 파일 경로

    private int itemCount;  //상품수량

    public CartResponseDto(Cart cart) {
        cartId = cart.getCartId();
        itemId = cart.getItem().getItemId();
        itemName = cart.getItem().getItemName();
        itemContent = cart.getItem().getItemContent();
        itemPrice = cart.getItem().getItemPrice();
        itemFilepath = cart.getItem().getItemFilepath();
        itemCount = cart.getItemCount();
    }
}
