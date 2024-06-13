package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.ItemQna;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemQnaRepository {

    @PersistenceContext
    private EntityManager em;

    //==생성|수정==//
    public void save(ItemQna itemQna) {
        if(itemQna.getId() == null) {
            em.persist(itemQna);  //새로운 엔티티를 영속성 컨텍스트에 저장하고, 데이터베이스에 삽입
        } else {
            em.merge(itemQna); //비영속 상태의 엔티티를 영속성 컨텍스트에 병합하거나, 이미 영속성 컨텍스트에 있는 엔티티와 병합
        }
    }

    //==조회==//
    public ItemQna findById(Long id) {
        return em.find(ItemQna.class, id);
    }

    public List<ItemQna> findAllByItemId(Long itemId, Pageable pageable) {
        //JPQL 쿼리
        String jpql = "select i from itemQna join fetch i.item where i.itemId = :id order by i.id desc";

        TypedQuery<ItemQna> query = em.createQuery(jpql, ItemQna.class)
                .setParameter("id", itemId)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    public List<ItemQna> findAllByMemberId(Long memberId, Pageable pageable) {
        //JPQL
        String jpql = "select i from itemQna join fetch i.member where m.memberId = :id order by i.id desc";

        TypedQuery<ItemQna> query = em.createQuery(jpql, ItemQna.class)
                .setParameter("id", memberId)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    //==삭제==//
    public void delete(ItemQna itemQna) {
        em.remove(itemQna);
    }
}
