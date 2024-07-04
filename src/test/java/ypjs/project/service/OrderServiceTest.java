package ypjs.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Address;
import ypjs.project.domain.Order;
import ypjs.project.domain.enums.OrderStatus;
import ypjs.project.dto.deliverydto.DeliveryCreateDto;
import ypjs.project.dto.orderdto.OrderCreateDto;
import ypjs.project.dto.orderdto.OrderItemRequestDto;
import ypjs.project.dto.orderdto.OrderResponseDto;
import ypjs.project.dto.orderdto.OrderSearchDto;
import ypjs.project.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void  상품주문() throws Exception {
        /*Given*/

        //멤버번호
        Long memberId = 1L;

        //배송정보
        Address address = new Address("주소", "상세", "12345");
        DeliveryCreateDto d = new DeliveryCreateDto("A", "01012345678", address);

        //주문상품리스트
        List<OrderItemRequestDto> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemRequestDto(1L, 1, 1000));

        //주문DTO생성
        OrderCreateDto o = new OrderCreateDto(d, orderItems);

        /*When*/
        Long orderId = orderService.create(1L, o);

        /*Then*/
        Order findOrder = orderRepository.findOne(orderId);

        //테스트 실패시
        assertEquals("상품 주문시 상태는 주문완료", OrderStatus.주문완료,
                findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.",1,
                findOrder.getOrderItems().size());
        
        //테스트 성공시
        System.out.println("테스트 성공");
    }

    @Test
    public void  주문내역조회() throws Exception {
        Pageable pageable = PageRequest.of(1,5);

        List<OrderResponseDto> orders = orderService.findAllByMemberId(1L, pageable, new OrderSearchDto());

        for(OrderResponseDto o : orders) {
            System.out.println(o);
        }
    }

}