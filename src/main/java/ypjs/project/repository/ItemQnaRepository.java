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
        if(itemQna.getItemQnaId() == null) {
            em.persist(itemQna);  //새로운 엔티티를 영속성 컨텍스트에 저장하고, 데이터베이스에 삽입
        } else {
            em.merge(itemQna); //비영속 상태의 엔티티를 영속성 컨텍스트에 병합하거나, 이미 영속성 컨텍스트에 있는 엔티티와 병합
        }
    }

    //==조회==//
    public ItemQna findOne(Long itemQnaId) {
        return em.find(ItemQna.class, itemQnaId);
    }

    public List<ItemQna> findAllByItemId(Long itemId, Pageable pageable) {
        return em.createQuery("select i from ItemQna i join fetch i.item where i.item.itemId = :id order by i.itemQnaId desc"
                        , ItemQna.class)
                .setParameter("id", itemId)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

    }

    public List<ItemQna> findAllByMemberId(Long memberId, Pageable pageable) {
        return em.createQuery("select i from ItemQna i join fetch i.qMember where i.qMember.memberId = :id order by i.itemQnaId desc"
                        , ItemQna.class)
                .setParameter("id", memberId)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

    }

    public List<ItemQna> findAll(Pageable pageable) {
        return em.createQuery("select i from ItemQna i order by i.status desc, i.itemQnaId desc"
                , ItemQna.class)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    //==총 개수 조회==//
    public int countAll() {
        return em.createQuery("select count(i) from ItemQna i", Long.class)
                .getSingleResult()
                .intValue();
    }

    public int countByItemId(Long itemId) {
        return em.createQuery("select count(i) from ItemQna i where i.item.itemId = :id", Long.class)
                .setParameter("id", itemId)
                .getSingleResult()
                .intValue();
    }

    public int countByMemberId(Long memberId) {
        return em.createQuery("select count(i) from ItemQna i where i.qMember.memberId = :id", Long.class)
                .setParameter("id", memberId)
                .getSingleResult()
                .intValue();
    }

    //==삭제==//
    public void delete(ItemQna itemQna) {
        em.remove(itemQna);
    }
}
