package ypjs.project.dto.orderdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.dto.deliverydto.DeliveryDto;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderCreateDto {

    private Long memberId;  //멤버번호

    private DeliveryDto deliveryDto;  //배송정보

    private List<OrderItemDto> orderItems;  //주문상품DTO리스트

    public OrderCreateDto(Long memberId, DeliveryDto deliveryDto, List<OrderItemDto> orderItems) {
        this.memberId = memberId;
        this.deliveryDto = deliveryDto;
        this.orderItems = orderItems;
    }

}
