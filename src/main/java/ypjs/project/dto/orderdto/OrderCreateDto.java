package ypjs.project.dto.orderdto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    @NotNull(message = "배송 정보가 없습니다.")
    private DeliveryCreateDto deliveryCreateDto;  //배송정보

    @NotNull(message = "주문상품 정보가 없습니다.")
    private List<OrderItemRequestDto> orderItemRequestDtos;  //주문상품요청DTO리스트

}
