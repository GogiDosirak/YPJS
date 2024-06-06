package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.*;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.dto.OrderCreateDto;
import ypjs.project.dto.OrderSearchDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.OrderRepository;

import java.util.List;

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
        Member member = memberRepository.findOne(orderCreateDto.getMemberId());

        //주소 객체 생성
        Address address = new Address(
                orderCreateDto.getDeliveryDto().getAddress(),
                orderCreateDto.getDeliveryDto().getAddressDetail(),
                orderCreateDto.getDeliveryDto().getZipcode()
        );

        //배송정보 생성
        Delivery delivery = new Delivery(
                orderCreateDto.getDeliveryDto().getName(),
                orderCreateDto.getDeliveryDto().getReceiver(),
                orderCreateDto.getDeliveryDto().getPhoneNumber(),
                address,
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

    //==주문 취소==//
    @Transactional
    public void cancel(Long orderId) {

        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }

    //==주문 검색==//
    public List<Order> search(OrderSearchDto orderSearchDto) {
        return orderRepository.findAllWithStatusOrMemberName(orderSearchDto);
    }

}
