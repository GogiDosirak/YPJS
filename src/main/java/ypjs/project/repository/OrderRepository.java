package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ypjs.project.domain.Order;

import java.util.List;

@Repository
@RequiredArgsConstructor  //final 필드 또는 @NonNull 필드를 초기화하는 생성자 자동 생성
public class OrderRepository {

    @PersistenceContext  //엔티티 메니저 주입
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public void delete(Order order) {
        em.remove(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }


    public List<Order> findAll(Pageable pageable, String orderStatus) {
        String jpql = "select o from Order o";

        if(StringUtils.hasText(orderStatus)) {
            jpql += " where o.status = :status";
        }

        jpql += " order by o.orderId desc";

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        if(StringUtils.hasText(orderStatus)) {
            query.setParameter("status", orderStatus);
        }

        return query.getResultList();
    }


    public List<Order> findAllByMemberId(Long memberId, Pageable pageable, String orderStatus) {
        //JPQL 쿼리
        String jpql = "select o from Order o join fetch o.member m where m.memberId = :id";

        //orderStatus 조건이 있는 경우 조건 추가
        if(StringUtils.hasText(orderStatus)) {
                jpql += " and o.status = :status";
        }

        jpql += " order by o.orderId desc";

        //쿼리 생성
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setParameter("id", memberId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        //orderStatus 조건이 있는 경우 매개변수 대입
        if(StringUtils.hasText(orderStatus)) {
            query.setParameter("status", orderStatus);
        }

        //결과 리스트 가져와서 리턴
        return query.getResultList();
    }


    //==총 개수 조회==//
    public int countAll() {
        return em.createQuery("select count(o) from Order o", Long.class)
                .getSingleResult()
                .intValue();
    }


    public int countByMemberId(Long memberId) {
        return em.createQuery("select count(o) from Order o where o.member.memberId = :id", Long.class)
                .setParameter("id", memberId)
                .getSingleResult()
                .intValue();
    }

/*
    public List<Order> findAllByStatusOrMemberName(OrderSearchDto orderSearchDto) {
        //JPQL
        String jpql = "select o from order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if(orderSearchDto.getOrderStatus() != null) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //멤버 이름 검색
        if(StringUtils.hasText(orderSearchDto.getMemberName())) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);  //검색 결과 최대 1000건

        if(orderSearchDto.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearchDto.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearchDto.getMemberName())) {
            query = query.setParameter("name", orderSearchDto.getMemberName());
        }

        return query.getResultList();
    }
 */

}
