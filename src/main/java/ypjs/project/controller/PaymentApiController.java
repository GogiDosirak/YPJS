package ypjs.project.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.dto.paymentdto.PaymentDto;
import ypjs.project.dto.paymentdto.UpdatePointsRequest;
import ypjs.project.service.MemberService;
import ypjs.project.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/payment")
public class PaymentApiController {

    private final PaymentService paymentService;
    private final MemberService memberService;


    //결제응답//결제요청은 OrderController create-> order.js
    //응답 엔티티
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> validationPayment(@RequestBody PaymentCallbackRequest paymentCallbackRequest, HttpServletRequest request) throws Exception {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(paymentCallbackRequest);

        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        HttpSession session = request.getSession();
        // 결제 성공 시 세션에 paymentUid 저장
        session.setAttribute("paymentUid", paymentCallbackRequest.getPaymentUid());

        // 응답 객체에 redirectUrl을 포함시킵니다.
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("iamportResponse", iamportResponse);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    //결제 취소
    @DeleteMapping("/cancel/{payId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelPayment(@PathVariable(name = "payId") Long payId){
        try {
            paymentService.cancelPayment(payId);
            return ResponseEntity.ok("주문 취소가 성공적으로 처리되었습니다.");
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //결제 중단 시 실행하는 메서드(order 랑 payment 삭제)
    @PostMapping("/fail-payment")
    public ResponseEntity<String> handlePaymentFailure(@RequestBody PaymentDto.FailPaymentDto failPaymentDTO) {

        String errorMessage = paymentService.deleteOrderAndPayment(failPaymentDTO);

        System.out.println(errorMessage);

        return ResponseEntity.ok("중단된 결제 관련 정보를 삭제했습니다.");
    }


    //payment 포인트 사용 함수
    @PostMapping("/updateMemberPoints")
    public ResponseEntity<String> updateMemberPoints(@RequestBody UpdatePointsRequest request) {

        // 회원 포인트 업데이트 서비스 호출
        boolean updated = memberService.updateMemberPoints(request);

        if (updated) {
            return ResponseEntity.ok("회원 포인트 업데이트 성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 포인트 업데이트 실패");
        }
    }
}
