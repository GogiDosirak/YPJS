package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.domain.Member;
import ypjs.project.domain.Page;
import ypjs.project.dto.ResponseDto;
import ypjs.project.domain.enums.Role;
import ypjs.project.dto.cartdto.CartListDto;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.orderdto.OrderAdminDto;
import ypjs.project.dto.orderdto.OrderResponseDto;
import ypjs.project.dto.orderdto.OrderSearchDto;
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
public class OrderViewController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;


    //상품상세 -> 주문 생성 페이지==//
    @GetMapping("/createFromItem")
    public String createFromItem(@RequestParam(name = "itemId") String itemId, Model model, HttpServletRequest request) throws  Exception {
        System.out.println("**상품상세 -> 주문 생성 페이지 요청됨");

        HttpSession session = request.getSession();
        List<CartListDto> cartListDtos = (List<CartListDto>) session.getAttribute("cartList");

        /*주문 상품이 없는 경우*/
        if(cartListDtos == null) {
            request.setAttribute("msg", "잘못된 접근입니다.");
            request.setAttribute("url", "/ypjs/item/get/" + itemId);
            return "alert";
        }

        //멤버정보 -> 배송정보 생성
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");
        Member m = memberService.findOne(loginMember.getMemberId());

        model.addAttribute("delivery", new DeliveryCreateDto(m.getName(), m.getPhonenumber(), m.getAddress()));
        model.addAttribute("cartList", cartListDtos);

        //데이터 전달 후 제거
        session.removeAttribute("cartList");

        System.out.println("**세션 주문상품 삭제 확인-> " + session.getAttribute("cartList"));

        return "order/create";

    }


    //장바구니 -> 주문 생성 페이지==//
    @GetMapping("/createFromCart")
    public String createFromCart(Model model, HttpServletRequest request) throws Exception{
        System.out.println("**장바구니 -> 주문 생성 페이지 요청됨");
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

        session.setAttribute("cartIds", cartIds);

        //멤버정보 -> 배송정보 생성
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");
        Member m = memberService.findOne(loginMember.getMemberId());

        model.addAttribute("delivery", new DeliveryCreateDto(m.getName(), m.getPhonenumber(), m.getAddress()));
        model.addAttribute("cartList", cartListDtos);

        //데이터 전달 후 제거
        session.removeAttribute("cartList");

        System.out.println("**세션 주문상품 삭제 확인-> " + session.getAttribute("cartList"));
        System.out.println("**세션 장바구니ID 목록 확인-> " + session.getAttribute("cartIds"));

        return "order/create";
    }


    //==주문 전체 조회(관리자)==//
    @GetMapping("/list/admin")
    public String listAdmin(
            HttpServletRequest request, Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @ModelAttribute OrderSearchDto orderSearchDto) {
        System.out.println("**주문 전체 조회(관리자) 요청됨");

        HttpSession session = request.getSession();
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");

        Member member = memberService.findOne(loginMember.getMemberId());

        //로그인 멤버가 관리자가 아닌 경우
        if(member.getRole() != Role.ROLE_ADMIN) {
            request.setAttribute("msg", "잘못된 접근입니다.");
            request.setAttribute("url", "/index");
            return "alert";
        }

        deliveryService.updateStatus();

        Pageable pageable = PageRequest.of(page, size);
        List<OrderAdminDto> o = orderService.findAll(pageable, orderSearchDto);

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
            HttpServletRequest request, Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @ModelAttribute OrderSearchDto orderSearchDto) {
        System.out.println("**멤버별 주문 전체 조회 요청됨");

        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) request.getSession().getAttribute("member");
        Long memberId = loginMember.getMemberId();

        Pageable pageable = PageRequest.of(page, size);
        List<OrderResponseDto> o = orderService.findAllByMemberId(memberId, pageable, orderSearchDto);

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


}
