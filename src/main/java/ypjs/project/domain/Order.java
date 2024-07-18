package ypjs.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime created;  //주문생성시간

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;  //주문상태

    @JsonIgnore
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @Column(name = "order_uid")
    private String orderUid;


    //==연관관계 메서드==//
    private void setDeliveryOrder(Delivery delivery) {
        delivery.setOrder(this);
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //==취소 메서드==//
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.배송중) {
            throw new IllegalStateException("배송중인 상품은 취소가 불가능합니다.");
        } else if(delivery.getStatus() == DeliveryStatus.배송완료) {
            throw new IllegalStateException("배송완료된 상품은 취소가 불가능합니다.");
        }
        this.status = OrderStatus.주문취소;
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


    //==생성 메서드==//
    public static Order create(Member member, Delivery delivery, List<OrderItem> orderItems) {
        int totalPrice = 0;
        Order order = new Order();
        order.member = member;
        order.delivery = delivery;
        order.setDeliveryOrder(delivery);
        for(OrderItem oi : orderItems) {
            order.addOrderItem(oi);
            totalPrice += oi.totalPrice();
        }
        order.price = totalPrice;
        order.created = LocalDateTime.now();
        order.status = OrderStatus.결제대기중;

        order.orderUid = UUID.randomUUID().toString();

        return order;
    }

//==payment 연관 메서드==//

    //==상태 변경 메서드==//
    public Long updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
        return this.orderId;
    }

    public Long updateOrderCreated(LocalDateTime payDate) {
        this.created = payDate;
        return this.orderId;
    }


    //== 주문 아이템 이름 정보 메서드 ==//
    public String getOrderItemsNameInfo() {
        if (orderItems.isEmpty()) {
            return "주문 상품이 없습니다.";
        }

        OrderItem firstOrderItem = orderItems.get(0);
        String firstItemName = firstOrderItem.getItem().getItemName();
        int remainingItemCount = orderItems.size() - 1;

        return firstItemName + " 외 " + remainingItemCount + "개";
    }

//==payment 연관 메서드 끝==//
}