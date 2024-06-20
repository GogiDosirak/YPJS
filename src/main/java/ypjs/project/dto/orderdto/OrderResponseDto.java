package ypjs.project.dto.orderdto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.Order;
import ypjs.project.domain.OrderItem;
import ypjs.project.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseDto {

    @NotNull(message = "")
    private Long orderId;  //주문번호

    @NotNull
    private Delivery delivery;  //배송정보

    @NotNull
    private int price;  //주문금액

    @NotNull
    private LocalDateTime created;  //주문시간

    @NotNull
    private OrderStatus status;  //주문상태

    @NotNull
    private List<OrderItemResponseDto> orderItems;  //주문상품리스트

    public OrderResponseDto(Order order) {
        orderId = order.getOrderId();
        delivery = order.getDelivery();
        price = order.getPrice();
        created = order.getCreated();
        status = order.getStatus();
        orderItems = new ArrayList<>();
        for(OrderItem oi : order.getOrderItems()) {
            addOrderItem(oi);
        }
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(
                OrderItemResponseDto.create(orderItem)
        );
    }

}
