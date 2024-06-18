package ypjs.project.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.paymentdto.PaymentCallbackRequest;
import ypjs.project.dto.paymentdto.RequestPayDto;
import ypjs.project.service.PaymentService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/payment") //공통으로 매핑에 주소 경로 추가
public class PaymentController {

    private final PaymentService paymentService;

    //오더1111
    @GetMapping("/")
    public String home(){
        return "payment/home";
    }

    //오더222
    @GetMapping("/order")
    public String order( @RequestParam(name = "orderId", required = false) String orderId,
                        Model model) {

        model.addAttribute("orderId", orderId);

        return "payment/order";
    }

    //오더333
    //html 작동 확인을 위한 더미데이터 주입함
    // payment.order.html 화면에서 폼 안에 입력해서 테스트, 주문이 만약 누군가가 1번으로 한게 있으면 오류남
    @PostMapping("/order")
    public String findOrder(@RequestParam(name="orderId") String orderId) {

        //주문생성 여기서?

        return "redirect:/ypjs/payment/payment/" + orderId;
    }

    //결제 창으로 연결하는 메서드
    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable(name = "orderId", required = false) Long orderId,
                              Model model) {
        paymentService.findAndCreatePayment(orderId);
        RequestPayDto requestPayDto = paymentService.makeRequestPayDto(orderId);
        model.addAttribute("requestPayDto", requestPayDto);
        return "payment/payment";
    }

    //결제응답
    //응답 엔티티
    @ResponseBody
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);

        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }


    //결제 성공시 화면 연결
    @GetMapping("/success-payment")
    public String successPaymentPage() {
        return "payment/success-payment";
    }

    //결제 실패시 화면 연결
    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        //결제 실패한걸 오더 기록에 남기는 메서드
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

        return "payment/list";
    }




}