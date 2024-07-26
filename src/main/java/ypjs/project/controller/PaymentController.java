package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.paymentdto.PaymentDto;
import ypjs.project.dto.paymentdto.RequestPayDto;
import ypjs.project.service.PaymentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/payment") //공통으로 매핑에 주소 경로 추가
public class PaymentController {

    private final PaymentService paymentService;

    //결제 창으로 연결하는 메서드
    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable(name = "orderId") Long orderId , Model model, HttpSession session) {

        //오더 아이디가 없을 시 메인 페이지로 넘기기 //오류페이지
        if (orderId == null) {
            return "redirect:/index";
        }

        //로그인 한 유저 정보 받아오기 //다른 사람이면 메이 페이지로 넘기기 // 오류페이지
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");
        PaymentDto.checkLoginMemberOrderMemberDto dto = paymentService.checkLoginMemberAndOrderMember(responseLogin.getMemberId(), orderId);
        if(dto.isCheck()){
            paymentService.createPayment(orderId);
            RequestPayDto requestPayDto = paymentService.makeRequestPayDto(orderId);
            model.addAttribute("requestPayDto", requestPayDto);

            return dto.getMessage();
        }
        return dto.getMessage();

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



    //    @GetMapping("/findOnePayment")
//    @ResponseBody
//    public ypjs.project.domain.Payment findPayPaymentUid(@RequestParam(name = "payId") Long payId) {
//        return paymentService.findOnePayment(payId);
//    }



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