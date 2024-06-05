package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long orderId;  //주문번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //멤버번호

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = " delivery_id")
    private Delivery delivery;  //배송정보

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();  //주문상품리스트

    @Column(name = "order_price")
    private int orderPrice;  //주문금액

    @Column(name = "order_created")
    private LocalDateTime orderCreated;  //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;  //주문상태 [ORDER, CANCEL]


    //==연관관계 메서드==//
    private void setDeliveryOrder(Delivery delivery) {
        delivery.setOrder(this);
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }


    //==생성 메서드==//
    public Order create(Member member, Delivery delivery, OrderItem... orderItems) {
        int totalPrice = 0;
        Order order = new Order();
        order.member = member;
        order.delivery = delivery;
        order.setDeliveryOrder(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            totalPrice += orderItem.getOrderItemPrice();
        }
        order.orderPrice = totalPrice;
        order.orderCreated = LocalDateTime.now();
        order.orderStatus = OrderStatus.ORDER;

        return order;
    }


    //==취소 메서드==//
    public void cancel() {
        if(delivery.getDeliveryStatus() == DeliveryStatus.SHIPPING) {
            throw new IllegalStateException("배송중인 상품은 취소가 불가능합니다.");
        } else if(delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("배송완료된 상품은 취소가 불가능합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


}