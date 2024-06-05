package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

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
    private Member member;  //멤버

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();  //주문상품리스트

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = " delivery_id")
    private Delivery delivery;  //배송정보

    @Column(name = "order_price")
    private int orderPrice;  //주문금액

    @Column(name = "order_created")
    private LocalDateTime orderCreated;  //주문시간

    @Enumerated(EnumType.STRING)
    private CommonEnum.OrderStatus orderStatus;  //주문상태 [ORDER, CANCEL}

}