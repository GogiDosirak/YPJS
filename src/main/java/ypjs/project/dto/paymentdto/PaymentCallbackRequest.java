package ypjs.project.dto.paymentdto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // 데이터베이스에 payment_Id로 저장하는 전략
public class PaymentCallbackRequest {
    private String paymentUid; // 결제 고유 번호
    private String orderUid; // 주문 고유 번호
    private int usedPoint; //사용한 포인트
}
