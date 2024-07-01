package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Page;
import ypjs.project.dto.cartdto.CartListDto;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;
import ypjs.project.dto.orderdto.OrderAdminDto;
import ypjs.project.dto.orderdto.OrderCreateDto;
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.orderdto.OrderResponseDto;
import ypjs.project.service.DeliveryService;
import ypjs.project.service.MemberService;
import ypjs.project.service.OrderService;
import ypjs.project.service.PaymentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/order")  //클래스 내의 모든 핸들러 메서드에 대해 공통된 경로를 지정
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    //임시 로케이션
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    //==주문 생성 페이지==//
    @GetMapping("/create")
    public String createPage(Model model, HttpServletRequest request) throws Exception{
        System.out.println("**주문 생성 페이지 요청됨");
        //System.out.println(model.getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 소실
        //System.out.println(request.getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 소실
        //System.out.println(request.getSession().getAttribute("orderItemList")); AJAX 응답을 거치면서 데이터 유지

        HttpSession session = request.getSession();
        List<CartListDto> cartListDtos = (List<CartListDto>) session.getAttribute("cartList");

        /*주문 상품이 없는 경우*/
        if(cartListDtos == null) {
            request.setAttribute("msg", "잘못된 접근입니다.");
            request.setAttribute("url", "/ypjs/cart/list");
            return "alert";
        }

        //결제완료 시 장바구니 삭제할 cartId 리스트
        List<Long> cartIds = new ArrayList<>();

        for(CartListDto c : cartListDtos) {
            cartIds.add(c.getCartId());
        }

        session.setAttribute("cardIds", cartIds);

        //멤버정보 -> 배송정보 생성
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        //Member m = memberService.findById(memberId);
        Member m = memberService.findOne(1L);

        model.addAttribute("delivery", new DeliveryCreateDto(m.getName(), m.getPhonenumber(), m.getAddress()));
        model.addAttribute("cartList", cartListDtos);

        //데이터 전달 후 제거
        session.removeAttribute("cartList");

        System.out.println("**세션 주문상품 삭제 확인-> " + session.getAttribute("cartList"));
        System.out.println("**세션 장바구니ID 목록 확인-> " + session.getAttribute("cardIds"));

        return "order/create";
    }


    //==주문 생성==//
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid OrderCreateDto orderCreateDto, HttpServletRequest request) {
        System.out.println("**주문 생성 로직 요청됨");
        //Long memberId = (Long) request.getSession().getAttribute("loginMemberId");

        System.out.println(orderCreateDto.getDeliveryCreateDto());
        System.out.println(orderCreateDto.getOrderItemRequestDtos());

        Long orderId = orderService.create(1L, orderCreateDto);

        System.out.println("**세션 장바구니ID 목록 전달 확인_> " + request.getSession().getAttribute("cardIds"));

        return ResponseEntity.ok().body(orderId);

    }


    //==주문 전체 조회(관리자)==//
    @GetMapping("/list/admin")
    public String listAdmin(
            HttpSession session, Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "orderStatus", defaultValue = "") String orderStatus) {
        System.out.println("**주문 전체 조회(관리자) 요청됨");

        Pageable pageable = PageRequest.of(page, size);
        List<OrderAdminDto> o = orderService.findAll(pageable, orderStatus);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(orderService.countAll(), size);

        model.addAttribute("orderList", o);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("today", LocalDateTime.now());
        return "order/listAdmin";
    }


    //==멤버별 주문 전체 조회==//
    @GetMapping("/list")
    public String list(
            HttpSession session, Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "orderStatus", defaultValue = "") String orderStatus) {
        System.out.println("**멤버별 주문 전체 조회 요청됨");
        //Long memberId = (Long) session.getAttribute("loginMemberId");
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(page, size);
        List<OrderResponseDto> o = orderService.findAllByMemberId(memberId, pageable, orderStatus);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(orderService.countByMemberId(memberId), size);

        model.addAttribute("orderList", o);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("today", LocalDateTime.now());
        return "order/list";
    }

    //==주문 상세 조회==//
    @GetMapping("/detail")
    public String detail(@RequestParam("orderId") Long orderId, Model model) {
        System.out.println("**주문 상세 조회 요청됨");

        OrderResponseDto o = orderService.findOne(orderId);
        //결제 내역 찾아서 model에 추가해야 한다.

        model.addAttribute("order", o);

        return "order/detail";
    }


    //==취소==//
    @GetMapping("/cancel/{orderId}")
    public ResponseDto<?> cancel(Long orderId) {
        System.out.println("**주문 취소 로직 요청됨");
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
