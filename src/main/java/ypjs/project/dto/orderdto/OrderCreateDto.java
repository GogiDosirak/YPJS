package ypjs.project.dto.orderdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    private DeliveryCreateDto deliveryCreateDto;  //배송정보

    private List<OrderItemRequestDto> orderItemRequestDtos;  //주문상품요청DTO리스트

}
