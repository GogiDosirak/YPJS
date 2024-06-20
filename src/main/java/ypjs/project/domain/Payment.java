package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ypjs.project.domain.enums.PayStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "PAYMENT")
@NoArgsConstructor
public class Payment {
    @Id @GeneratedValue
    @Column(name = "pay_id")
    private Long payId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private int payPrice;

    private String payName;

    private String payPhoneNumber;

    private String payEmail;

    private LocalDateTime payDate; //결제 날짜

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus; //OK, READY, CANCEL

    private String payPaymentUid; //결제 고유 번호

    //==생성 메서드==//

    public static Payment createPayment(Order order, int payPrice, String payName, String payPhoneNumber, String payEmail) {
        Payment payment = new Payment();
        payment.order = order;
        payment.payPrice = payPrice;
        payment.payName = payName;
        payment.payPhoneNumber = payPhoneNumber;
        payment.payEmail = payEmail;
        payment.payDate = LocalDateTime.now();
        payment.payStatus = PayStatus.READY;
        return payment;
    }

    //==결제 완료 상태처리 메서드==//
    public void changePaymentBySuccess(PayStatus payStatus, String payPaymentUid){
        this.payStatus = payStatus;
        this.payPaymentUid = payPaymentUid;
    }

    //==결제 취소 상태처리 메서드==//
    public void changePaymentStatusCanceled(){
        if(payStatus.equals(PayStatus.READY)){
            throw new IllegalStateException("결제 전의 주문은 취소가 불가능 합니다.");
        }else if(payStatus.equals(PayStatus.CANCEL)){
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        this.payStatus = PayStatus.CANCEL;
    }
}