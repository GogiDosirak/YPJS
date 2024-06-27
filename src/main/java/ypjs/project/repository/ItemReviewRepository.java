package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.ItemReview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemReviewRepository {

    private final EntityManager em;


    //리뷰저장
    public void saveReview(ItemReview itemReview) {
            em.persist(itemReview);
    }


    //리뷰 하나 조회
    public ItemReview findOneReview(Long itemReviewId) {
        return em.find(ItemReview.class, itemReviewId);
    }



    //리뷰 삭제
    public void deleteItemReview(Long itemReviewId) {
        ItemReview findItemReivew = em.find(ItemReview.class, itemReviewId);
        em.remove(findItemReivew);
    }





    //아이템 당 리뷰 조회
    public List<ItemReview> findAllItemReview(Long itemId, Pageable pageable) {
        return em.createQuery(
                        "select ir from ItemReview ir" +
                                " join fetch ir.item i WHERE i.itemId = :itemId order by itemReviewId desc", ItemReview.class)
                .setParameter("itemId", itemId)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }



    //아이템리뷰 총 개수
    public int countAllItemReview(Long itemId){
       return em.createQuery("select count(distinct ir) from ItemReview ir join ir.item i where i.itemId = :itemId", Long.class)
                .setParameter("itemId", itemId)
                .getSingleResult()
                .intValue();

    }

}
