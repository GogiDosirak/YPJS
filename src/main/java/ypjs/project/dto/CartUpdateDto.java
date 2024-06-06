package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartUpdateDto {

    @NotNull
    private Long cartId;  //장바구니번호

    @NotNull
    private int itemCount;  //상품수량



}
