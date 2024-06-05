package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
@Getter
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long cartId;  //장바구니번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //멤버번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  //상품번호

    @Column(name = "cart_item_quantity")
    private int cartItemQuantity;  //장바구니상품수량

    @Column(name = "cart_added")
    private LocalDateTime cartAdded;  //장바구니추가일시
}
