package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    //저장
    public void save(Order order) {
            em.persist(order);
    }

    public Order findOrder(Long orderId) {
        return em.find(Order.class, orderId);
    }
}
