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
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.cartdto.CartListDto;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.orderdto.OrderAdminDto;
import ypjs.project.dto.orderdto.OrderCreateDto;
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
@RequestMapping("/api/ypjs/order")  //클래스 내의 모든 핸들러 메서드에 대해 공통된 경로를 지정
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;


    //==상품 주문하기==//
    @PostMapping("/item")
    public ResponseEntity<Void> item(@RequestBody @Valid List<CartListDto> cartListDtos, HttpServletRequest request) {
        System.out.println("**상품상세 상품 주문 요청됨");
        System.out.println(cartListDtos);

        request.getSession().setAttribute("cartList", cartListDtos);

        return ResponseEntity.ok().build();
    }


    //==주문 생성==//
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid OrderCreateDto orderCreateDto, HttpServletRequest request) {
        System.out.println("**주문 생성 로직 요청됨");

        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) request.getSession().getAttribute("member");

        System.out.println(orderCreateDto.getDeliveryCreateDto());
        System.out.println(orderCreateDto.getOrderItemRequestDtos());

        Long orderId = orderService.create(loginMember.getMemberId(), orderCreateDto);

        System.out.println("**세션 장바구니ID 목록 전달 확인_> " + request.getSession().getAttribute("cartIds"));

        return ResponseEntity.ok().body(orderId);

    }


}
