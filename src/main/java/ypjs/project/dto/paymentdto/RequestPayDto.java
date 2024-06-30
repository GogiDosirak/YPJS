package ypjs.project.dto.paymentdto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class) //데이터베이스에 저장할 떄 order_id 로 저장되게 하는 어노테이션
@AllArgsConstructor
public class RequestPayDto {
    private Long orderId;
    private String orderUid;  //주문Uid
    private String itemName; //주문상품
    private String payName; //구매자 이름
    private int orderPrice; //주문금액
    private String payEmail; //주문자 이메일
    private String deliveryAddress; //주문 주소
    private String phoneNumber; //주문자 전화번호
    private String zipcode; // 주문자 주소2
    private int point; //포인트
    private Long memberId; //주문자 아이디

}