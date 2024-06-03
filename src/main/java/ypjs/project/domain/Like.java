package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "LIKES")
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private int likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    public Like(Member member, Item item){
        this.member = member;
        this.item = item;
    }

}