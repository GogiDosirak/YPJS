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

    @Column(name = "cart_item_count")
    private Integer itemCount;  //상품수량

    public Cart(Member member, Item item, int itemCount) {
        this.member = member;
        this.item = item;
        this.itemCount = itemCount;
    }

    //테스트용 생성자 3개
    public Cart() {
    }

    public Cart(Long cartId) {
        this.cartId = cartId;
    }

    public Cart(Long cartId, Member member, Item item, int itemCount) {
        this.cartId = cartId;
        this.member = member;
        this.item = item;
        this.itemCount = itemCount;
    }

    public void updateItemCount(int itemCount) {
        this.itemCount = itemCount;
    }


}
