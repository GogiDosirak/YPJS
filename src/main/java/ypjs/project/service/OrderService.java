package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.*;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.dto.orderdto.OrderCreateDto;
import ypjs.project.dto.orderdto.OrderItemRequestDto;
import ypjs.project.dto.orderdto.OrderResponseDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.OrderRepository;

import java.util.ArrayList;
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
    public Long create(Long memberId, OrderCreateDto orderCreateDto) {
        //멤버정보 생성
        Member member = memberRepository.findOne(memberId);

        //배송정보 생성
        Delivery delivery = new Delivery(
                orderCreateDto.getDeliveryDto().getReceiver(),
                orderCreateDto.getDeliveryDto().getPhoneNumber(),
                orderCreateDto.getDeliveryDto().getAddress(),
                DeliveryStatus.배송준비중
        );

        //주문상품리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequestDto oid : orderCreateDto.getOrderItemRequestDtos()) {
            orderItems.add(
                    OrderItem.create(
                            itemRepository.findOne(oid.getItemId()),
                            oid.getItemCount(),
                            oid.getItemPrice()
                    )
            );
        }

        //주문 생성
        Order order = Order.create(member, delivery, orderItems);

        //주문 저장
        orderRepository.save(order);
        return order.getOrderId();

    }

    //==멤버별 주문 전체 조회==//
    public List<OrderResponseDto> findAllByMemberId(Long memberId, Pageable pageable, String orderStatus) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId, pageable, orderStatus);

        //Page<Order> -> Page<OrderResponseDto> 변환
        return orders.stream()
                .map(order -> new OrderResponseDto(order))
                .collect(toList());
    }

    //==주문 1건 조회==//
    public OrderResponseDto findOne(Long orderId) {
        Order order = orderRepository.findOne(orderId);

        return new OrderResponseDto(order);
    }


    //==주문 취소==//
    @Transactional
    public void cancel(Long orderId) {

        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

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