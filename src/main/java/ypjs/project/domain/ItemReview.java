package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class ItemReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Lob
    @Column(name = "ITEM_REVIEW_CONTENT", columnDefinition = "LONGTEXT")
    private String itemReviewContent;

    @Column(name = "ITEM_CREATEDATE")
    @CreatedDate
    private LocalDateTime itemReviewCreateDate;


    //생성자

    public ItemReview() {}

    public ItemReview(Item item,Member member, int itemScore, String itemReviewName, String itemReviewContent) {

        this.item = item;
        this.member = member;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
        this.itemReviewCreateDate = LocalDateTime.now();
    }



    //리뷰변경 메서드
    public Long changeItemReview(int itemScore, String itemReviewName, String itemReviewContent) {
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