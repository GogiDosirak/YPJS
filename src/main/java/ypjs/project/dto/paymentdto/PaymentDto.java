package ypjs.project.dto.paymentdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class OrderIdDto {
        private Long orderId;
    }


}
