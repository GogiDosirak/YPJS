package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ypjs.project.domain.Order;
import ypjs.project.dto.orderdto.OrderSearchDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
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


    public List<Order> findAll(Pageable pageable, OrderSearchDto orderSearchDto) {
        String jpql = "select o from Order o join fetch o.member m join fetch o.orderItems oi";

        jpql += searchQuery(orderSearchDto, true);

        jpql += " order by o.orderId desc";

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        query = searchQuerySetParam(query, orderSearchDto);

        return query.getResultList();
    }


    public List<Order> findAllByMemberId(Long memberId, Pageable pageable, OrderSearchDto orderSearchDto) {
        //JPQL 쿼리
        String jpql = "select o from Order o join fetch o.member m where m.memberId = :id";

        jpql += searchQuery(orderSearchDto, false);

        jpql += " order by o.orderId desc";

        //쿼리 생성
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setParameter("id", memberId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        query = searchQuerySetParam(query, orderSearchDto);

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

    private String searchQuery(OrderSearchDto orderSearchDto, boolean isFirstCondition) {
        String jpql = "";

        //주문 생성 시작일 검색
        if(orderSearchDto.getStartDate() != null && orderSearchDto.getEndDate() == null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.created >= :startDate";
        }

        //주문 생성 종료일 검색
        if(orderSearchDto.getEndDate() != null && orderSearchDto.getStartDate() ==null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.created <= :endDate";
        }

        //주문 생성 시작~종료일 검색
        if(orderSearchDto.getEndDate() != null && orderSearchDto.getStartDate() !=null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.created >= :startDate and o.created <= :endDate";
        }

        //주문 상태 검색
        if(orderSearchDto.getSearchOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :orderStatus";
        }

        //배송 상태 검색
        if(orderSearchDto.getSearchDeliveryStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.delivery.status = :deliveryStatus";
        }

        //주문 번호 검색
        if(orderSearchDto.getSearchOrderId() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.orderId = :orderId";
        }

        //주문 고객ID 검색
        if(StringUtils.hasText(orderSearchDto.getSearchMemberUserName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.username like :username";
        }

        //주문 상품명 검색
        if(StringUtils.hasText(orderSearchDto.getSearchOrderItemName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " oi.item.itemName like :itemName";
        }

        return jpql;
    }

    private TypedQuery<Order> searchQuerySetParam(TypedQuery<Order> query, OrderSearchDto orderSearchDto) {
        if(orderSearchDto.getStartDate() != null && orderSearchDto.getEndDate() == null) {
            query.setParameter("startDate", LocalDateTime.of(orderSearchDto.getStartDate(), LocalTime.of(0, 0)));
        }
        if(orderSearchDto.getEndDate() != null && orderSearchDto.getStartDate() ==null) {
            query.setParameter("endDate", LocalDateTime.of(orderSearchDto.getEndDate(), LocalTime.of(24, 0)));
        }
        if(orderSearchDto.getStartDate() != null && orderSearchDto.getEndDate() !=null) {
            query.setParameter("startDate", LocalDateTime.of(orderSearchDto.getStartDate(), LocalTime.of(0, 0)))
                 .setParameter("endDate", LocalDateTime.of(orderSearchDto.getEndDate(), LocalTime.of(23, 59)));
        }
        if (orderSearchDto.getSearchOrderStatus() != null) {
            query.setParameter("orderStatus", orderSearchDto.getSearchOrderStatus());
        }
        if (orderSearchDto.getSearchDeliveryStatus() != null) {
            query.setParameter("deliveryStatus", orderSearchDto.getSearchDeliveryStatus());
        }
        if (orderSearchDto.getSearchOrderId() != null) {
            query.setParameter("orderId", orderSearchDto.getSearchOrderId());
        }
        if (StringUtils.hasText(orderSearchDto.getSearchMemberUserName())) {
            query.setParameter("username", "%" + orderSearchDto.getSearchMemberUserName() + "%");
        }
        if (StringUtils.hasText(orderSearchDto.getSearchOrderItemName())) {
            query.setParameter("itemName", "%" +  orderSearchDto.getSearchOrderItemName() + "%");
        }

        return query;
    }

}
