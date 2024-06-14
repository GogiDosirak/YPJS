package ypjs.project.dto.paymentdto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class) //데이터베이스에 저장할 떄 order_id 로 저장되게 하는 어노테이션
public class RequestPayDto {
    private Long orderId;  //주문번호
    private String itemName; //주문상품 이름
    private String payName; //구매자 이름
    private int orderPrice; //주문금액
    private String payEmail; //구매자 이메일
    private String deliveryAddress; //주문 주소

//생성자
    public RequestPayDto(Long orderId, String itemName, String payName, int orderPrice, String payEmail, String deliveryAddress) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.payName = payName;
        this.orderPrice = orderPrice;
        this.payEmail = payEmail;
        this.deliveryAddress = deliveryAddress;
    }
}