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
import ypjs.project.service.CartService;
import ypjs.project.service.MemberService;
import ypjs.project.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/payment")
public class PaymentApiController {

    private final PaymentService paymentService;
    private final MemberService memberService;
    private final CartService cartService;



    //결제성공//결제요청은 OrderController create-> order.js
    //응답 엔티티
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> validationPayment(@RequestBody PaymentCallbackRequest paymentCallbackRequest, HttpServletRequest request) throws Exception {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(paymentCallbackRequest);

        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        HttpSession session = request.getSession();
        // 결제 성공 시 세션에 paymentUid 저장
        session.setAttribute("paymentUid", paymentCallbackRequest.getPaymentUid());

        // 결제 성공 시 세션에 cart가 있는지 확인 후 삭제
        // 세션에서 cartIds 가져오기
        Object cartIdsObj = session.getAttribute("cartIds");

        // cartIds가 List<Long> 형태인지 확인하고 처리하기
        if (cartIdsObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<Long> cartIds = (List<Long>) cartIdsObj;

            // 각 cartId를 순회하며 삭제
            for (Long cartId : cartIds) {
                if (cartId != null) {
                    cartService.delete(cartId);
                }
            }

            // cartIds를 세션에서 삭제하거나 빈 리스트로 초기화할 수 있습니다.
            session.removeAttribute("cartIds");
        }
        // 결제 성공 시 세션에 cart가 있는지 확인 후 삭제 끝

        // 응답 객체에 redirectUrl을 포함시킵니다.
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("iamportResponse", iamportResponse);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    //결제 취소(결제완료o)
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


    //결제 중단 시(결제완료x) 실행하는 메서드(관련 order 랑 payment DB에서 삭제)
    @PostMapping("/fail-payment")
    public ResponseEntity<String> handlePaymentFailure(@RequestBody PaymentDto.FailPaymentDto failPaymentDTO) {

        //payment 삭제
        paymentService.deletePayment(failPaymentDTO);

        System.out.println(failPaymentDTO.getErrorMessage());

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