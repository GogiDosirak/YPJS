package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;  //주문상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  //상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  //주문번호

    @Column(name = "order_item_count")
    private int count;  //주문상품수량

    @Column(name = "order_item_price")
    private int price;  //주문상품가격


    //==연관관계 메서드==//
    public void setOrder(Order order) {
        this.order = order;
    }


    //==생성 메서드==//
    public static OrderItem create(Item item, int itemCount, int itemPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.count = itemCount;
        orderItem.price = itemPrice;
        item.removeStock(itemCount);  //item 재고 제거 메서드 연결
        return orderItem;
    }

    //총 가격 계산 메서드==//
    public int totalPrice() {
        return this.count * this.price;
    }


    //==취소 메서드==//
    public void cancel() {
        getItem().addStock(count);  //item 재고 추가 메서드 연결
    }



}