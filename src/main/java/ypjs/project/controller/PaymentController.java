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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/payment") //공통으로 매핑에 주소 경로 추가
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/")
    public String home(){
        return "payment/home";
    }

    @GetMapping("/order")
    public String order( @RequestParam(name = "orderId", required = false) String orderId,
                        Model model) {

        model.addAttribute("orderId", orderId);

        return "payment/order";
    }

    //html 작동 확인을 위한 더미데이터 주입함
    // payment.order.html 화면에서 폼 안에 입력해서 테스트, 주문이 만약 누군가가 1번으로 한게 있으면 오류남
    @PostMapping("/order")
    public String findOrder(@RequestParam(name="orderId") String orderId) {

        //주문생성 여기서?

        return "redirect:/ypjs/payment/payment/" + orderId;
    }

    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable(name = "orderId", required = false) Long orderId,
                              Model model) {

        RequestPayDto requestPayDto = paymentService.makeRequestPayDto(orderId);
        model.addAttribute("requestPayDto", requestPayDto);
        return "payment/payment";
    }

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
        return "payment/fail-payment";
    }


}