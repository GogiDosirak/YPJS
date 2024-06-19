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

    // 주문 정보를 DTO로 변환하는 메서드
    public RequestPayDto makeRequestPayDto(Long orderId) {
        Order order = findOrder(orderId);
        return createRequestPayDto(order);
    }

    // 주문 정보를 기반으로 DTO를 생성하는 메서드
    private RequestPayDto createRequestPayDto(Order order) {
        // DTO 를 생성하여 반환
        return new RequestPayDto(
                order.getOrderId(),
                order.getOrderItems().toString(),
                order.getMember().getName(),
                order.getPrice(),
                order.getMember().getEmail(),
                order.getDelivery().getDeliveryAddress().getAddress()+ " " + order.getDelivery().getDeliveryAddress().getAddressDetail(),// 구매자 주소
                order.getMember().getPhonenumber(),
                order.getDelivery().getDeliveryAddress().getZipcode()
        );
    }

    //주문 저장 및 결제 생성 메서드
    @Transactional
    public Long findAndCreatePayment(Long orderId) {
        // 이미 결제된 주문인지 확인
        ypjs.project.domain.Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment != null && existingPayment.getPayStatus().equals(PayStatus.OK)) {
            throw new IllegalStateException("이미 완료된 주문입니다");
        }

        // 주문 정보 조회
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다");
        }

        // 주문 정보가 없으면 예외 처리

        // 주문자 정보 조회
        Member member = order.getMember();

        // 결제 정보 생성 및 저장
        ypjs.project.domain.Payment payment = ypjs.project.domain.Payment.createPayment(order, order.getPrice(), member.getName(), member.getPhonenumber(), member.getEmail());
        paymentRepository.save(payment);

        return payment.getPayId();
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
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request){
        try {

            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());

            Order order = paymentRepository.findOrderAndPayment(request.getOrderUid())
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
