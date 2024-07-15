package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Payment;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final EntityManager em;

    //저장//왜때문인지 이렇게 안하면 API 오류남
    public void save(Payment payment) {
        if (payment.getPayId() == null) {
            em.persist(payment);
        } else {
            em.merge(payment);
        }
    }

    //==Payment 관련 쿼리
    public Optional<Order> findOrderAndPaymentAndMember(Long orderId) {
        return em.createQuery(
                        "select o from Order o" +
                                " left join fetch o.payment p" +
                                " left join fetch o.memberId m" +
                                " where o.orderId = :orderId", Order.class)
                .setParameter("orderId", orderId)
                .getResultStream()
                .findFirst();
    }

    public Optional<Order> findOrderAndPayment(Long orderId) {
        return em.createQuery(
                        "select o from Order o" +
                                " left join fetch o.payment p" +
                                " where o.orderId = :orderId", Order.class)
                .setParameter("orderId", orderId)
                .getResultStream()
                .findFirst();
    }
}
