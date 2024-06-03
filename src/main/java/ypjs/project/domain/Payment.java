package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "PAYMENT")
@NoArgsConstructor
public class Payment {
    @Id @GeneratedValue
    @Column(name = "pay_id")
    private int payId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private int payPrice;

    private String payName;

    private String payPhonenumber;

    private String payEmail;

    private LocalDateTime payDate; //결제 날짜

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus; //OK, READY, CANCEL

    private String payPaymentUid; //결제 고유 번호

    //==생성 메서드==//

    public static Payment createPayment(Order order, int payPrice, String payName, String payPhonenumber, String payEmail, LocalDateTime payDate, PayStatus payStatus, String payPaymentUid) {
        Payment payment = new Payment();
        payment.order = order;
        payment.payPrice = payPrice;
        payment.payName = payName;
        payment.payPhonenumber = payPhonenumber;
        payment.payEmail = payEmail;
        payment.payDate = payDate;
        payment.payStatus = payStatus;
        payment.payPaymentUid = payPaymentUid;
        return payment;
    }

    //==결제 상태처리 메서드==//
    public void changePaymentBySuccess(PayStatus payStatus, String payPaymentUid){
        this.payStatus = payStatus;
        this.payPaymentUid = payPaymentUid;
    }



}