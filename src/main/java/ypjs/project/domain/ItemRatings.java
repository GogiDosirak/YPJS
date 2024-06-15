package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ItemRatings {

    @Id @GeneratedValue
    @Column(name = "ITEM_RATING_ID")
    private Long ItemRatingId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "ITEM_SCORE")
    private Integer ItemScore;

}