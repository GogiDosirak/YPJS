package ypjs.project.dto.cartdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartAddDto {

    private Long memberId;  //멤버번호

    @NotNull(message = "상품 정보가 없습니다.")
    private Long itemId;  //상품번호

    @NotNull(message = "수량 정보가 없습니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int itemCount;  //상품수량



}
