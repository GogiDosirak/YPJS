package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import ypjs.project.domain.enums.ItemQnaStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_qna")
@Getter
public class ItemQna {

    @Id
    @GeneratedValue
    @Column(name = "item_qna_id")
    private Long itemQnaId;  //상품문의번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item itemId;  //상품번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "q_member_id")
    private Member qMemberId;  //질문멤버번호

    @Column(name = "item_qna_q")
    private String itemQnaQ;  //상품문의질문

    @Column(name = "item_qna_q_created")
    private LocalDateTime itemQnaQCreated;  //상품문의질문작성일시

    @Column(name = "item_qna_q_updated")
    private LocalDateTime itemQnaQUpdated;  //상품문의질문수정일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_member_id")
    private Member aMemberId;  //답변멤버번호

    @Column(name = "item_qna_a")
    private String itemQnaA;  //상품문의답변

    @Column(name = "item_qna_a_created")
    private LocalDateTime itemQnaACreated;  //상품문의답변작성일시

    @Column(name = "item_qna_a_updated")
    private LocalDateTime itemQnaAUpdated;  //상품문의답변수정일시

    @Enumerated
    @Column(name = "item_qna_status")
    private ItemQnaStatus itemQnaStatus;  //상품문의상태
}