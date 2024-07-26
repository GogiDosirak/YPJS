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
import ypjs.project.domain.enums.OrderStatus;
import ypjs.project.domain.enums.PayStatus;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.dto.paymentdto.PaymentDto;
import ypjs.project.dto.paymentdto.RequestPayDto;
import ypjs.project.repository.OrderRepository;
import ypjs.project.repository.PaymentRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;

    //회원찾기
    @Transactional
    public Optional<Order> findOrderByUid (String orderUid){
        return paymentRepository.findOrderAndPayment(orderUid);}

    //결제 성공 메서드
    @Transactional
    public PaymentDto.SuccessPaymentDto findPaymentByPaymentUid(String paymentUid){
        ypjs.project.domain.Payment payment = paymentRepository.findPaymentByPaymentUid(paymentUid);
        //주문상태 변경
        payment.getOrder().updateOrderStatus(OrderStatus.주문완료);
        //결제완료 시간으로 order 업데이트
        payment.getOrder().updateOrderCreated(payment.getPayDate());
        return new PaymentDto.SuccessPaymentDto(payment.getOrder().getOrderId(), payment.getPayPrice(), payment.getPayDate());
    }

    //결제 단건 제거 메서드
    @Transactional
    public void delete(Long payId) {
        ypjs.project.domain.Payment payment = paymentRepository.findOne(payId);
        paymentRepository.delete(payment);
    }

    //주문 생성
    @Transactional
    public RequestPayDto makeRequestPayDto(Long orderId){
        Order order = paymentRepository.findOrderAndPaymentAndMember(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문이 없습니다."));

        // DTO 를 생성하여 반환
        return new RequestPayDto(
                order.getOrderId(),
                order.getOrderUid(), // 주문Uid
                order.getOrderItemsNameInfo(), // 주문상품 이름
                order.getMember().getName(), // 주문자 이름
                order.getPrice(), // 주문 금액
                order.getMember().getEmail(), // 주문자 이메일
                order.getDelivery().getAddress().getAddress()+ " " + order.getDelivery().getAddress().getAddressDetail(),// 구매자 주소
                order.getMember().getPhonenumber(), //주문자 전화번호
                order.getDelivery().getAddress().getZipcode(),//주문자 집코드
                order.getMember().getPoint(), //주문자 포인트
                order.getMember().getMemberId() //주문자 구분
        );
    }

    //주문 저장메서드
    @Transactional
    public Long createPayment(Long orderId){

        //주문 조회
        ypjs.project.domain.Payment findPayment = paymentRepository.findByOrderId(orderId);

        //이미 주문정보가 있을 때
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

    //payId로 payment 찾기
    @Transactional
    public ypjs.project.domain.Payment findOnePayment(Long payId){
        ypjs.project.domain.Payment payment = paymentRepository.findOne(payId);
        if (payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다. payId: " + payId);
        }
        return payment;
    }

    //결제 중단 시 오더랑 페이먼트 삭제 메서드
    @Transactional
    public void deletePayment(PaymentDto.FailPaymentDto failPaymentDTO){
        Order order = paymentRepository.findOrderAndPayment(failPaymentDTO.getOrderUid())
                .orElseThrow(()->new IllegalArgumentException("주문 내역이 없습니다."));

        //cascade 로 연관된 order 도 같이 지워짐
        paymentRepository.delete(order.getPayment());

    }

    //결제 끝나고 아임포트 결제 취소, 결제 상태 취소로 업데이트 메서드
    @Transactional
    public void cancelPayment(Long payId){
        ypjs.project.domain.Payment payment = paymentRepository.findOne(payId);

        if(payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다. payId: " + payId);
        }

        try {
            // 결제건 조회 후 취소
            iamportClient.cancelPaymentByImpUid(new CancelData(payment.getPayPaymentUid(), true, new BigDecimal(payment.getPayPrice())));
            payment.changePaymentStatusCanceled();
            //order status 주문 취소로 변경
            payment.getOrder().updateOrderStatus(OrderStatus.주문취소);
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 결제 내역 조회 메서드
    @Transactional
    public List<ypjs.project.domain.Payment> findPaymentsByMemberId(Long memberId, int offset, int limit) {
        return paymentRepository.findByOrderMemberId(memberId, offset, limit);
    }

    //결제 내역 조회 페이징을 위한 결제 갯수 메서드
    @Transactional
    public long countPaymentsByMemberId(Long memberId) {
        return paymentRepository.countByOrderMemberId(memberId);
    }

    //아임포트랑 연동, 리턴타임의 Payment 는 아임포트에서 제공하는 클래스임
    //결제 완료 후 반환 메서드
    @Transactional
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

            int usedPoint = request.getUsedPoint();
            System.out.println("사용한 포인트"+usedPoint);

            int price = order.getPayment().getPayPrice();
            System.out.println("DB에 저장된 결제 금액"+price);

            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();
            System.out.println("실 결제 금액"+iamportPrice);

            // 결제 금액 검증
            if(iamportPrice != price-usedPoint) {

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));
                ypjs.project.domain.Payment payment = order.getPayment();
                payment.changeStatus(PayStatus.CANCEL);

                throw new RuntimeException("결제금액 위변조 의심");
            }

            //결제된 금액으로 payPrice 변경
            order.getPayment().changePayPrice(iamportPrice);

            // 결제 상태 변경
            order.getPayment().changePaymentUidAndStatusAndPayDate(PayStatus.OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //OrderDetail 용 결제 내역 찾는 메서드
    @Transactional
    public PaymentDto.PaymentForOrderDetailDto PaymentForOrderDetail (Long orderId){

        ypjs.project.domain.Payment payment = paymentRepository.findByOrderId(orderId);

        return new PaymentDto.PaymentForOrderDetailDto(payment);
    }

    //로그인한 사람이랑 order 를 쓰는 사람이랑 같은지 확인하는 로직
    @Transactional
    public PaymentDto.checkLoginMemberOrderMemberDto checkLoginMemberAndOrderMember(Long memberId, Long orderId){
        PaymentDto.checkLoginMemberOrderMemberDto dto = new PaymentDto.checkLoginMemberOrderMemberDto();

        Order order = orderRepository.findOne(orderId);

        boolean check = order.getMember().getMemberId().equals(memberId);

        dto.setCheck(check);
        if(check){
            dto.setMessage("payment/payment");
        }else {
            dto.setMessage("redirect:/index");
        }

        return dto;

    }

}