package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    //private final MemberRepository memberRepository;
    //private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /*
    @Transactional
    public Long createOrder(Long memberId, Long itemId, int count, DeliveryDto deliveryDto) {

        //배송정보 생성
        Delivery delivery = new Delivery(deliveryDto.getAddress(), DeliveryStatus.READY);

    }

     */
}
