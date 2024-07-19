package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ypjs.project.domain.enums.ItemQnaStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_qna")
@Getter
@NoArgsConstructor
public class ItemQna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_qna_id")
    private Long itemQnaId;  //상품문의번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  //상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "q_member_id")
    private Member qMember;  //질문멤버번호

    @Column(name = "item_qna_q")
    private String q;  //상품문의질문

    @Column(name = "item_qna_q_created")
    private LocalDateTime qCreated;  //상품문의질문작성일시

    @Column(name = "item_qna_q_updated")
    private LocalDateTime qUpdated;  //상품문의질문수정일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_member_id")
    private Member aMember;  //답변멤버번호

    @Column(name = "item_qna_a")
    private String a;  //상품문의답변

    @Column(name = "item_qna_a_created")
    private LocalDateTime aCreated;  //상품문의답변작성일시

    @Column(name = "item_qna_a_updated")
    private LocalDateTime aUpdated;  //상품문의답변수정일시

    @Enumerated(EnumType.STRING)
    @Column(name = "item_qna_status")
    private ItemQnaStatus status;  //상품문의상태


    //==생성자==//
    public ItemQna(Item item, Member qMember, String question) {
        this.item = item;
        this.qMember = qMember;
        this.q = question;
        this.qCreated = LocalDateTime.now();
        this.status = ItemQnaStatus.PENDING;
    }

    //==메서드==//
    //연관관계 메서드
    public void changeItem(Item item) {
        this.item = item;
    }

    //답변 작성 및 수정
    public void answer(Member aMember, String a) {
        this.aMember = aMember;
        this.a = a;
        if(aCreated == null) {
            this.aCreated = LocalDateTime.now();
        } else {
            this.aUpdated = LocalDateTime.now();
        }
        this.status = ItemQnaStatus.ANSWERED;
    }

    //질문 수정
    public void updateQ(String q) {
        this.q = q;
        this.qUpdated = LocalDateTime.now();
        this.status = ItemQnaStatus.PENDING;
    }

}