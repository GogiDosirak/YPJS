package ypjs.project.dto.paymentdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class PaymentDto {
    @Data
    @AllArgsConstructor
    public static class SuccessPaymentDto {
        private Long orderId;
        private int payPrice;
        private LocalDateTime payDate;
    }


}
