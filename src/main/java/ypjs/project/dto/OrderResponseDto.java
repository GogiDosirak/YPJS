package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.Order;
import ypjs.project.domain.OrderItem;
import ypjs.project.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponseDto {

    @NotNull(message = "")
    private Long orderId;  //주문번호

    @NotNull
    private Delivery delivery;  //배송정보

    @NotNull
    private List<OrderItem> orderItems;  //주문상품리스트

    @NotNull
    private int price;  //주문금액

    @NotNull
    private LocalDateTime created;  //주문시간

    @NotNull
    private OrderStatus status;  //주문상태

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.delivery = order.getDelivery();
        this.orderItems = order.getOrderItems();
        this.price = order.getPrice();
        this.created = order.getCreated();
        this.status = order.getStatus();
    }

}
