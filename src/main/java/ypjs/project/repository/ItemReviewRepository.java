package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.ItemReview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemReviewRepository {

    private final EntityManager em;


    //리뷰저장
    public void saveReview(ItemReview itemReview) {
        if (itemReview.getItemReviewId() == null) {
            em.persist(itemReview);
        } else {
            em.merge(itemReview);
        }
    }


    //리뷰 하나 조회
    public ItemReview findOneReview(Long itemReviewId) {
        return em.find(ItemReview.class, itemReviewId);
    }

    //리뷰 전체조회
    public List<ItemReview> findAllReviews() {
        return em.createQuery("select ir from ItemReview ir", ItemReview.class)
                .getResultList();
    }



}
