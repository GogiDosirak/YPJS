package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "LIKES")
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    //==생성 메서드==//
    public Like(Member member, Item item){
        this.member = member;
        this.item = item;
    }


}