package ypjs.project.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.dto.paymentdto.PaymentDto;
import ypjs.project.dto.paymentdto.RequestPayDto;
import ypjs.project.dto.paymentdto.UpdatePointsRequest;
import ypjs.project.service.MemberService;
import ypjs.project.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/payment") //공통으로 매핑에 주소 경로 추가
public class PaymentController {

    private final PaymentService paymentService;
    private final MemberService memberService;

    //결제 창으로 연결하는 메서드
    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable(name = "orderId", required = false) Long orderId,
                              Model model) {
        paymentService.findAndCreatePayment(orderId);
        RequestPayDto requestPayDto = paymentService.makeRequestPayDto(orderId);
        model.addAttribute("requestPayDto", requestPayDto);

        return "payment/payment";
    }

    //결제응답//결제요청은 OrderController create-> order.js
    //응답 엔티티
    @ResponseBody
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

    @GetMapping("/findOnePayment")
    @ResponseBody
    public ypjs.project.domain.Payment findPayPaymentUid(@RequestParam(name = "payId") Long payId) {
        return paymentService.findOnePayment(payId);
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


    // 결제 성공 페이지로 이동
    @GetMapping("/success-payment")
    public String successPaymentPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 세션에서 paymentUid 가져오기
        String paymentUid = (String) session.getAttribute("paymentUid");

        // 세션에서 paymentUid 삭제
        session.removeAttribute("paymentUid");

        // 결제 정보 조회
        PaymentDto.SuccessPaymentDto payment = paymentService.findPaymentByPaymentUid(paymentUid);

        model.addAttribute("payment", payment);

        return "payment/success-payment";
    }

    //결제 실패시 화면 연결
    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        //todo : 결제 실패한걸 오더 기록에 남기는 메서드
        return "payment/fail-payment";
    }

    // 결제 내역 조회 페이지
    @GetMapping("/list/{memberId}")
    public String listPayments(@PathVariable(name = "memberId") Long memberId,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               Model model) {
        int offset = page * size;
        List<ypjs.project.domain.Payment> payments = paymentService.findPaymentsByMemberId(memberId, offset, size);
        long totalPayments = paymentService.countPaymentsByMemberId(memberId);
        int totalPages = (int) Math.ceil((double) totalPayments / size);

        model.addAttribute("payments", payments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("memberId", memberId);  // 페이지네이션을 위해 memberId를 모델에 추가

        return "payment/list";
    }

    //payment 포인트 사용 함수
    @PostMapping("/updateMemberPoints")
    public ResponseEntity<String> updateMemberPoints(@RequestBody UpdatePointsRequest request) {
        Long memberId = request.getMemberId();
        int usedPoints = request.getUsedPoints();

        // 회원 포인트 업데이트 서비스 호출
        boolean updated = memberService.updateMemberPoints(memberId, usedPoints);

        if (updated) {
            return ResponseEntity.ok("회원 포인트 업데이트 성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 포인트 업데이트 실패");
        }
    }
    //payment 포인트 사용 함수 끝



//    //오더1111
//    @GetMapping("/")
//    public String home(){
//        return "payment/home";
//    }
//
//    //오더222
//    @GetMapping("/order")
//    public String order( @RequestParam(name = "orderId", required = false) Long orderId,
//                        Model model) {
//
//        model.addAttribute("orderId", orderId);
//
//        return "payment/order";
//    }
//
//    //오더333
//    //html 작동 확인을 위한 더미데이터 주입함
//    // payment.order.html 화면에서 폼 안에 입력해서 테스트, 주문이 만약 누군가가 1번으로 한게 있으면 오류남
//    @PostMapping("/order")
//    public String findOrder(@RequestParam(name="orderId") Long orderId) {
//
//        //주문생성 여기서?
//
//        return "redirect:/ypjs/payment/payment/" + orderId;
//    }
//

}