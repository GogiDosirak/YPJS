package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Cart;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    @PersistenceContext
    private final EntityManager em;

    //==생성|수정==//
    public void save(Cart cart) {
        if(cart.getCartId() == null) {
            em.persist(cart);  //새로운 엔티티를 영속성 컨텍스트에 저장하고, 데이터베이스에 삽입
        } else {
            em.merge(cart);  //비영속 상태의 엔티티를 영속성 컨텍스트에 병합하거나, 이미 영속성 컨텍스트에 있는 엔티티와 병합
        }
    }

    //==조회==//
    public Cart findOne(Long cartId) {
        return em.find(Cart.class, cartId);
    }

    //==멤버별 개수 조회==//
    public Long count(Long memberId) {
        return em.createQuery("select count(c) from Cart c where c.member.memberId = :id"
                        , Long.class)
                .setParameter("id", memberId)
                .getSingleResult();
    }

    //==멤버별 전체 조회==//
    public List<Cart> findAllByMemberId(Long memberId) {
        return em.createQuery("select c from Cart c join fetch c.member m where m.memberId = :id order by c.cartId desc"
                        , Cart.class)
                .setMaxResults(100)  //검색 결과 최대 100건
                .setParameter("id", memberId)
                .getResultList();
    }

    //==멤버별 중복 상품 조회==//
    public List<Long> findItemIdByMemberId(Long memberId) {
        return em.createQuery("select c.item.itemId from Cart c where c.member.memberId = :id"
                        , Long.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    //==삭제==//
    public void delete(Cart cart) {
        em.remove(cart);
    }
}
