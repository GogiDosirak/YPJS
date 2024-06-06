package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateDto {

    @NotNull(message = "")
    private Long memberId;  //멤버번호

    @NotNull
    private DeliveryDto deliveryDto;  //배송정보

    @NotNull
    private List<OrderItemDto> orderItems;  //주문상품DTO리스트

}
