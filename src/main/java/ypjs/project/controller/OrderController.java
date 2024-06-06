package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ypjs.project.dto.OrderCreateDto;
import ypjs.project.dto.OrderSearchDto;
import ypjs.project.service.OrderService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/order")  //클래스 내의 모든 핸들러 메서드에 대해 공통된 경로를 지정
public class OrderController {

    private final OrderService orderService;

    //==주문 생성==//
    @PostMapping("/create")
    public void create(@RequestBody @Valid OrderCreateDto orderCreateDto) {
        orderService.create(orderCreateDto);
    }

    //==검색 조회==//
    @PostMapping("/search")
    public void search(@RequestBody @Valid OrderSearchDto orderSearchDto) {
        orderService.search(orderSearchDto);
    }
}
