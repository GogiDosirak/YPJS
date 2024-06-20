package ypjs.project.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Member;
import ypjs.project.domain.Order;
import ypjs.project.domain.enums.PayStatus;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.dto.paymentdto.RequestPayDto;
import ypjs.project.repository.OrderRepository;
import ypjs.project.repository.PaymentRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;

    //회원찾기
    public Order findOrder (Long orderId) {
        return orderRepository.findOne(orderId);
    }

    //주문 생성
    public RequestPayDto makeRequestPayDto(Long orderId){
        Order order = paymentRepository.findOrderAndPaymentAndMember(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문이 없습니다."));

        // DTO 를 생성하여 반환
        return new RequestPayDto(
                order.getOrderId(), // 주문번호
                order.getOrderItemsNameInfo(), // 주문상품 이름
                order.getMember().getName(), // 주문자 이름
                order.getPrice(), // 주문 금액
                order.getMember().getEmail(), // 주문자 이메일
                order.getDelivery().getDeliveryAddress().getAddress()+ " " + order.getDelivery().getDeliveryAddress().getAddressDetail(),// 구매자 주소
                order.getMember().getPhonenumber(), //주문자 전화번호
                order.getDelivery().getDeliveryAddress().getZipcode() //주문자 집코드
        );
    }

    //주문 저장메서드
    public Long findAndCreatePayment(Long orderId){

        //이미 주문정보가 있을 때
        ypjs.project.domain.Payment findPayment = paymentRepository.findByOrderId(orderId);
        if(findPayment!=null){
            if(findPayment.getPayStatus().equals(PayStatus.OK)){
                throw new IllegalStateException("이미 완료된 주문입니다");
            }
            return findPayment.getPayId();
        }

        //주문정보 생성
        Order order = orderRepository.findOne(orderId);

        //주문자 정보 생성
        Member member = order.getMember();

        //주문 정보 생성
        ypjs.project.domain.Payment payment = ypjs.project.domain.Payment.createPayment(order, order.getPrice(), member.getName(), member.getPhonenumber(), member.getEmail());

        //주문 정보 저장
        paymentRepository.save(payment);
        return payment.getPayId();
    }

    public String findOnePaymentPayPaymentUid(Long paymentId){
        ypjs.project.domain.Payment payment = paymentRepository.findOne(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다. paymentId: " + paymentId);
        }
        return payment.getPayPaymentUid();
    }


    //결제 취소 메서드
    public void cancelPayment(Long payId){
        ypjs.project.domain.Payment payment = paymentRepository.findOne(payId);

        if(payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다. payId: " + payId);
        }

        try {
            // 결제건 조회 후 취소
            iamportClient.cancelPaymentByImpUid(new CancelData(payment.getPayPaymentUid(), true, new BigDecimal(payment.getPayPrice())));
            payment.changePaymentStatusCanceled();
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 결제 내역 조회 메서드
    public List<ypjs.project.domain.Payment> findPaymentsByMemberId(Long memberId, int offset, int limit) {
        return paymentRepository.findByOrderMemberId(memberId, offset, limit);
    }

    //결제 내역 조회 페이징을 위한 결제 갯수 메서드
    public long countPaymentsByMemberId(Long memberId) {
        return paymentRepository.countByOrderMemberId(memberId);
    }

    //아임포트랑 연동, 리턴타임의 Payment 는 아임포트에서 제공하는 클래스임
    //결제 완료 후 반환 메서드
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request){
        try {

            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());

            //todo : Long 값을 그대로 쓸것인가 새로운 String 필드를 만들어야하는가
            Order order = paymentRepository.findOrderAndPayment(Long.valueOf(request.getOrderId()))
                    .orElseThrow(()->new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            int price = order.getPayment().getPayPrice();
            System.out.println("DB에 저장된 결제 금액"+price);

            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();
            System.out.println("실 결제 금액"+iamportPrice);

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
