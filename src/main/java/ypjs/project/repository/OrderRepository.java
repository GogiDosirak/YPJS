package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Order;

@Repository
@RequiredArgsConstructor  //final 필드 또는 @NonNull 필드를 초기화하는 생성자 자동 생성
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    //public List<Orders> findAll(OrderSearch orderSearch) { ~ }

}
