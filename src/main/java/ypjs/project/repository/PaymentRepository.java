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

    //저장
    public void save(Payment payment) {em.persist(payment);}

    //결제 단건 조회
    public Payment findOne(Long payId) {
        return em.find(Payment.class, payId);
    }

    //결제 단건 제거
    public void delete(Payment payment) {
        em.remove(payment);
    }

    //==orderId로 payment 찾기
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

    // 회원의 결제 내역 조회
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

    // 회원의 총 결제 내역 수 조회
    public long countByOrderMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM Payment p " +
                                "JOIN p.order o " +
                                "JOIN o.member m " +
                                "WHERE m.memberId = :memberId", Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }


    //==Order 찾기(+ payment, member)
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

    //==orderUid 로 Order 찾기(+ payment)
    public Optional<Order> findOrderAndPayment(String orderUid) {
        return em.createQuery(
                        "select o from Order o" +
                                " left join fetch o.payment p" +
                                " where o.orderUid = :orderUid", Order.class)
                .setParameter("orderUid", orderUid)
                .getResultStream()
                .findFirst();
    }

    //==paymentUid 로 Payment 찾기
    public Payment findPaymentByPaymentUid(String paymentUid){
        return em.createQuery(
                        "select p from Payment p" +
                                " where p.payPaymentUid = :paymentUid", Payment.class)
                .setParameter("paymentUid",paymentUid)
                .getSingleResult();
    }
}