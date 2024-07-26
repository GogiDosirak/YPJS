package ypjs.project.dto.paymentdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Payment;
import ypjs.project.domain.enums.PayStatus;

import java.time.LocalDateTime;

public class PaymentDto {
    @Data
    @AllArgsConstructor
    public static class SuccessPaymentDto {
        private Long orderId;
        private int payPrice;
        private LocalDateTime payDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FailPaymentDto {
        private String orderUid;
        private String errorMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentForOrderDetailDto {
        private Long payId; //환불 로직 용
        private int payPrice; //포인트를 제외한 실 결제 금액
        private PayStatus payStatus; //현재 결제 내역 상태


        public PaymentForOrderDetailDto(Payment payment){
            this.payId = payment.getPayId();
            this.payPrice = payment.getPayPrice();
            this.payStatus = payment.getPayStatus();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class checkLoginMemberOrderMemberDto {
        private boolean check;
        private String message;
    }


}
