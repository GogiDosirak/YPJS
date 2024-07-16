package ypjs.project.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.enums.PayStatus;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.PaymentRepository;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

//    //임시 결제내역 생성 // 머지하고 수정예정
//    public void autoOrder(Member member) {
//
//        // 임시 결제내역 생성
//        ypjs.project.domain.Payment payment = ypjs.project.domain.Payment.builder()
//                .payId(1L)
//                .payPrice(10)
//                .payStatus(PayStatus.READY)
//                .build();
//
//        paymentRepository.save(payment);
//
//        //주소 임시 생성
//        Address memberAddress = Address.builder()
//                .address("서울시")
//                .addressDetail("강남구")
//                .zipcode("12345")
//                .build();
//
//        Delivery memberDelivery = Delivery.builder()
//                .deliveryAddress(memberAddress)
//                .deliveryId(12L)
//                .build();
//
//        //임시 주문 생성
//        Order order = Order.builder()
//                .member(member)
//                .orderPrice(10)
//                .orderId(1L)
//                .payment(payment)
//                .delivery(memberDelivery)
//                .build();
//
//        orderRepository.save(order);
//
//    }
//
//    //회원 자동 생성
//    public void autoRegister(){
//
//        //주소 임시 생성
//        Address memberAddress = Address.builder()
//                .address("서울시")
//                .addressDetail("강남구")
//                .zipcode("12345")
//                .build();
//
//        Member member = Member.builder()
//                .name(UUID.randomUUID().toString())
//                .email("example@example.com")
//                .address(memberAddress)
//                .build();
//        memberRepository.save(member);
//    }
//
//
//    //주문 생성
//    public RequestPayDto findRequestDto(Long orderId){
//        Order order = paymentRepository.findOrderAndPaymentAndMember(orderId)
//                .orElseThrow(()->new IllegalArgumentException("주문이 없습니다."));
//
//        // DTO 를 생성하여 반환
//        return new RequestPayDto(
//                order.getOrderId(), // 주문번호
//                order.getOrderItems().toString(), // 주문상품 번호
//                order.getMemberId().getName(), // 구매자 이름
//                order.getOrderPrice(), // 주문 금액
//                order.getMemberId().getEmail(), // 구매자 이메일
//                order.getDelivery().getDeliveryAddress().getAddress()+ order.getDelivery().getDeliveryAddress().getAddressDetail()+order.getDelivery().getDeliveryAddress().getZipcode()// 구매자 주소
//        );
//    }

    //아임포트랑 연동, 리턴타임의 Payment 는 아임포트에서 제공하는 클래스임
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request){
        try {

            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Order order = paymentRepository.findOrderAndPayment(request.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            int price = order.getPayment().getPayPrice();

            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(iamportPrice != price) {

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경
            order.getPayment().changePaymentBySuccess(PayStatus.OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
