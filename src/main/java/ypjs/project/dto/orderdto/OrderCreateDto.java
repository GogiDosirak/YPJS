package ypjs.project.dto.orderdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.dto.deliverydto.DeliveryDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    private DeliveryDto deliveryDto;  //배송정보

    private List<OrderItemDto> orderItemDtos;  //주문상품DTO리스트

}
