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
    public List<ItemReview> findAllItemReview(Long itemId, Pageable pageable, String sortBy) {
        // 기본 쿼리 문자열
        String queryString = "select ir from ItemReview ir join fetch ir.item i where i.itemId = :itemId";

        // 정렬 조건 추가
        switch (sortBy) {
            case "highScore":
                queryString += " order by ir.itemScore desc, ir.itemReviewId desc"; // 'highScore'일 때 itemScore 기준 내림차순, 동일할 경우 itemReviewId 내림차순 정렬
                break;
            case "lowScore":
                queryString += " order by ir.itemScore asc, ir.itemReviewId desc"; // 'lowScore'일 때 itemScore 기준 오름차순, 동일할 경우 itemReviewId 내림차순 정렬
                break;
            default:
                queryString += " order by ir.itemReviewId desc"; // 기본 정렬: itemReviewId 기준 내림차순 정렬 (최신순)
                break;
        }

        return em.createQuery(queryString, ItemReview.class)
                .setParameter("itemId", itemId)
                .setFirstResult((int) pageable.getOffset())
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
