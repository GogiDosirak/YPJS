package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
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
    private int price;  //주문금액

    @Column(name = "order_created")
    private LocalDateTime created;  //주문시간

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;  //주문상태 [ORDER, CANCEL]


    //==연관관계 메서드==//
    private void setDeliveryOrder(Delivery delivery) {
        delivery.setOrder(this);
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }


    //==생성 메서드==//
    public static Order create(Member member, Delivery delivery, List<OrderItem> orderItems) {
        int totalPrice = 0;
        Order order = new Order();
        order.member = member;
        order.delivery = delivery;
        order.setDeliveryOrder(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            totalPrice += orderItem.getTotalPrice();
        }
        order.price = totalPrice;
        order.created = LocalDateTime.now();
        order.status = OrderStatus.주문완료;  //!!결제완료 시 주문완료 되도록 수정 필요

        return order;
    }


    //==취소 메서드==//
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.SHIPPING) {
            throw new IllegalStateException("배송중인 상품은 취소가 불가능합니다.");
        } else if(delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("배송완료된 상품은 취소가 불가능합니다.");
        }
        this.status = OrderStatus.주문취소;
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


    //연관관계 메서드
    @JsonIgnore
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;
}