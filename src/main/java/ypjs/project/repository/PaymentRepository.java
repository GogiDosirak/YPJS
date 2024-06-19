package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Order;
import ypjs.project.domain.Payment;

import java.util.List;
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

    //==paymentId 로 오더 찾기
    public Payment findByOrderId(Long orderId) {
        try {
            return em.createQuery("SELECT p FROM Payment p" +
                            " JOIN FETCH p.order o" +
                            " WHERE o.orderId = :orderId", Payment.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 데이터가 없는 경우 null을 반환
        }
    }

    // 회원당 결제 내역 조회
    public List<Payment> findByOrderMemberId(Long memberId, int offset, int limit) {
        return em.createQuery(
                        "SELECT p FROM Payment p " +
                                "JOIN FETCH p.order o " +
                                "JOIN FETCH o.member m " +
                                "WHERE m.memberId = :memberId", Payment.class)
                .setParameter("memberId", memberId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // 회원 총 결제 내역 수 조회
    public long countByOrderMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM Payment p " +
                                "JOIN p.order o " +
                                "JOIN o.member m " +
                                "WHERE m.memberId = :memberId", Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }


    //==Payment 관련 쿼리
    public Optional<Order> findOrderAndPaymentAndMember(Long orderId) {
        return em.createQuery(
                        "select o from Order o" +
                                " left join fetch o.payment p" +
                                " left join fetch o.member m" +
                                " where o.orderId = :orderId", Order.class)
                .setParameter("orderId", orderId)
                .getResultStream()
                .findFirst();
    }

    public Optional<Order> findOrderAndPayment(String orderId) {
        return em.createQuery(
                        "select o from Order o" +
                                " left join fetch o.payment p" +
                                " where o.orderId = :orderId", Order.class)
                .setParameter("orderId", orderId)
                .getResultStream()
                .findFirst();
    }

    public Payment findPaymentByPaymentUid(String paymentUid){
        return em.createQuery(
                "select p from Payment p" +
                        " where p.payPaymentUid = :paymentUid", Payment.class)
                .setParameter("paymentUid", paymentUid)
                .getSingleResult();
    }
}
