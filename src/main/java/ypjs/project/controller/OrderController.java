package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.deliverydto.DeliveryDto;
import ypjs.project.dto.orderdto.OrderCreateDto;
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.orderdto.OrderItemDto;
import ypjs.project.service.MemberService;
import ypjs.project.service.OrderService;

import java.util.List;

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
    public String createPage(Model model, HttpServletRequest request) {
        //System.out.println(model.getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 소실
        //System.out.println(request.getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 소실
        //System.out.println(request.getSession().getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 유지

        //멤버정보 -> 배송정보 생성
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        //Member m = memberService.findById(memberId);
        Member m = memberService.findOne(1L);

        request.setAttribute("memberId", 1);  //request랑 model 중에 뭐가 더 데이터 전달에 용이한지 확인 후 고르기

        //model.addAttribute("memberId", 1);
        model.addAttribute("delivery", new DeliveryDto(m.getName(), m.getPhonenumber(), m.getAddress()));
        model.addAttribute("orderItemList", request.getSession().getAttribute("orderItemList"));

        request.getSession().removeAttribute("orderItemList");  //데이터 전달 후 제거
        System.out.println("**세션 주문상품 삭제 확인-> " + request.getSession().getAttribute("orderItemList"));

        return "order/create";
    }

    //==주문 생성==//
    @PostMapping("/create")
    public void create(@RequestBody @Valid OrderCreateDto orderCreateDto) {
        orderService.create(orderCreateDto);
    }


    //==멤버별 주문 전체 조회==//
    @GetMapping("/list")
    public String list(
            HttpSession session, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String orderStatus) {
        Long memberId = (Long) session.getAttribute("loginMemberId");
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("orderList", orderService.findAllByMemberId(memberId, pageable, orderStatus));
        return "#";
    }


    //==취소==//
    @GetMapping("/cancel/{orderId}")
    public ResponseDto<?> cancel(Long orderId) {
        orderService.cancel(orderId);
        return new ResponseDto<>(HttpStatus.OK.value(), "취소 완료");
    }

/*
    //==검색 조회==//
    @PostMapping("/search")
    public void search(@RequestBody @Valid OrderSearchDto orderSearchDto) {
        orderService.search(orderSearchDto);
    }
 */


}
