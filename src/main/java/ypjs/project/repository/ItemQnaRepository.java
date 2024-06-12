package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
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

    public List<ItemQna> findAllByItemId(Long itemId) {
        //JPQL
        String jpql = "select i from itemQna join fetch i.item where i.itemId = :id";

        TypedQuery<ItemQna> query = em.createQuery(jpql, ItemQna.class)
                .setMaxResults(1000);

        query = query.setParameter("id", itemId);

        return query.getResultList();
    }

    public List<ItemQna> findAllByMemberId(Long memberId) {
        //JPQL
        String jpql = "select i from itemQna join fetch i.member where m.memberId = :id";

        TypedQuery<ItemQna> query = em.createQuery(jpql, ItemQna.class)
                .setMaxResults(1000);  //검색 결과 최대 1000건

        query = query.setParameter("id", memberId);

        return query.getResultList();
    }

    //==삭제==//
    public void delete(ItemQna itemQna) {
        em.remove(itemQna);
    }
}
