package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.*;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.dto.OrderCreateDto;
import ypjs.project.dto.OrderResponseDto;
import ypjs.project.dto.OrderSearchDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.OrderRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    //==주문==//
    @Transactional
    public Long create(OrderCreateDto orderCreateDto) {
        //멤버정보 생성
        Member member = memberRepository.findById(orderCreateDto.getMemberId());

        //배송정보 생성
        Delivery delivery = new Delivery(
                orderCreateDto.getDeliveryDto().getReceiver(),
                orderCreateDto.getDeliveryDto().getPhoneNumber(),
                orderCreateDto.getDeliveryDto().getAddress(),
                DeliveryStatus.READY
        );

        //주문상품리스트 생성
        List<OrderItem> orderItems = orderCreateDto.getOrderItems().stream()
                .map(oI -> new OrderItem())
                .toList();

        //주문 생성
        Order order = Order.create(member, delivery, orderItems);

        //주문 저장
        orderRepository.save(order);
        return order.getId();

    }

    //==멤버별 주문 전체 조회==//
    public List<OrderResponseDto> findAllByMemberId(Long memberId, int page, int size, String orderStatus) {
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders = orderRepository.findAllByMemberId(memberId, pageable, orderStatus);

        //Page<Order> -> Page<OrderResponseDto> 변환
        //return orders.map(OrderResponseDto::new);
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(toList());
    }


    //==주문 취소==//
    @Transactional
    public void cancel(Long orderId) {

        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId);

        //주문 취소
        order.cancel();
    }

/*
    //==주문 검색==//
    public List<OrderResponseDto> search(OrderSearchDto orderSearchDto) {
        List<Order> orders =  orderRepository.findAllByStatusOrMemberName(orderSearchDto);
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(o -> new OrderResponseDto(o))
                .collect(toList());

        return orderResponseDtos;
    }
 */

}
