package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.OrderRepository;
import ypjs.project.service.PaymentService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
/*
    @GetMapping("/order")
    public String order(@RequestParam(name = "message", required = false) String message,
                        @RequestParam(name = "orderId", required = false) String id,
                        Model model) {

        model.addAttribute("message", message);
        model.addAttribute("orderId", id);

        return "order";
    }


    @PostMapping("/order")
    public String autoOrder() {

        paymentService.autoRegister();
        Member member = memberRepository.findOne(1L);

        paymentService.autoOrder(member);
        Order order = orderRepository.findOneOrder(1L);

        String message = "주문 실패";
        if(order != null) {
            message = "주문 성공";
        }

        String encode = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:/order?message="+encode+"&orderId="+order.getOrderId();
    }

    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) Long id,
                              Model model) {

        RequestPayDto requestPayDto = paymentService.findRequestDto(id);
        model.addAttribute("requestPayDto", requestPayDto);
        return "payment";
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
        return "success-payment";
    }

    //결제 실패시 화면 연결
    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        return "fail-payment";
    }

 */
}