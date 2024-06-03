package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private int orderItemId;  //주문상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item itemId;  //상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderId;  //주문번호

    @Column(name = "order_item_price")
    private int orderItemPrice;  //주문상품가격

    @Column(name = "order_item_quantity")
    private int orderItemQuantity;  //주문상품수량


}