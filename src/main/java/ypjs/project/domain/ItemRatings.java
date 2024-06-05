package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ItemRatings {

    @Id @GeneratedValue
    @Column(name = "ITEM_RATING_ID")
    private Long itemRatingId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "ITEM_SCORE")
    private Integer itemScore;


    //연관관계 메서드
    public void setItem(Item item) {
        this.item = item;
    }
}