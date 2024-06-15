package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ItemReview {

    @Id @GeneratedValue
    @Column(name = "ITEM_REVIEW_ID")
    private Long itemReviewId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "ITEM_SCORE")
    private int itemScore;

    @Column(name = "ITEM_REVIEW_NAME")
    private String itemReviewName;

    @Column(name = "ITEM_REVIEW_CONTENT")
    private String itemReviewContent;


    //생성자

    public ItemReview() {}

    public ItemReview(Item item,Member member, int itemScore, String itemReviewName, String itemReviewContent) {

        this.item = item;
        this.member = member;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
    }



    //리뷰변경 메서드
    public Long changeItemReview(Item item,Member member, int itemScore, String itemReviewName, String itemReviewContent) {
        this.item = item;
        this.member = member;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;

        return this.itemReviewId;
    }


    //연관관계 메서드
    public void setItem(Item item) {
        this.item = item;
    }
}