package ypjs.project.dto.cartdto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartAddDto {

    @NotNull
    private Long memberId;  //멤버번호

    @NotNull
    private Long itemId;  //상품번호

    @NotNull
    private int itemCount;  //상품수량



}