package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.DeliveryDto;
import ypjs.project.dto.OrderCreateDto;
import ypjs.project.dto.OrderSearchDto;
import ypjs.project.dto.ResponseDto;
import ypjs.project.service.MemberService;
import ypjs.project.service.OrderService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/order")  //클래스 내의 모든 핸들러 메서드에 대해 공통된 경로를 지정
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //==주문 생성 페이지==//
    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        //멤버정보 -> 배송정보 생성
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        //Member m = memberService.findById(memberId);
        Member m = memberService.findById(1L);

        model.addAttribute("orderCreateDto", new OrderCreateDto());
        model.addAttribute("deliveryDto", new DeliveryDto(m.getName(), m.getPhoneNumber(), m.getAddress()));
        model.addAttribute("orderItemDtos", model.getAttribute("orderItemDtos"));

        return "/order/orderFormTest";
    }

    //==주문 생성==//
    @PostMapping("/create")
    public void create(@RequestBody @Valid OrderCreateDto orderCreateDto) {
        orderService.create(orderCreateDto);
    }

    //==멤버별 주문 전체 조회==//
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("loginMemberId");
        model.addAttribute("orderList", orderService.findAllByMemberId(memberId));
        return "#";
    }

    //==검색 조회==//
    @PostMapping("/search")
    public void search(@RequestBody @Valid OrderSearchDto orderSearchDto) {
        orderService.search(orderSearchDto);
    }
    
    //==취소==//
    @GetMapping("/cancel/{orderId}")
    public ResponseDto<?> cancel(Long orderId) {
        orderService.cancel(orderId);
        return new ResponseDto<>(HttpStatus.OK.value(), "취소 완료");
    }
}
