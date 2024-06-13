package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;
import ypjs.project.dto.CategoryListDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;




    //상품저장
    public void saveItem(Item item) {
        //새로운 객체 생성(새로운 상품 등록)
        if(item.getItemId() == null) {
            em.persist(item);
        } else {
            //item.getItemId() != null이면 수정
            em.merge(item);
        }
    }


    //상품 하나 조회
    public Item findOne(Long ItemId) {
        return em.find(Item.class, ItemId);

    }



    //상품 여러개 조회
    public List<Item> findAllItems() {
       return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


    //상품 삭제
    public void deleteItem(Long itemId) {
        Item findItem = em.find(Item.class, itemId);
        em.remove(findItem);
    }






    //조회수
    @Transactional
    public void increaseCnt(Long itemId) {
        em.createQuery("update Item i set i.itemCnt = i.itemCnt + 1 where i.itemId = :itemId")
                .setParameter("itemId", itemId)
                .executeUpdate();
    }





    //(카테고리당 아이템 조회)
//    public List<Item> findAllItem(Long categoryId) {
//        return em.createQuery("select i from Item i where i.category.categoryId = :categoryId", Item.class)
//                .setParameter("categoryId", categoryId)
//                .getResultList();
//    }


    //페치조인(카테고리당 아이템 조회)
    public List<Item> findAllItem(Long categoryId) {
        return em.createQuery(
                        "select distinct i from Item i" +
                                " join fetch i.category c" +
                                " join fetch c.categoryParent cp" +
                                " where i.category.categoryId = :categoryId", Item.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }




    //카테고리 당 아이템 조회 기본 최신순 정렬, 후기 많은 순, 좋아요 많은 순 추가 정렬
    public List<Item> findAllItemSortBy(Long categoryId, int offset, int limit, String sortBy) {
        String queryString = "select distinct i from Item i" +
                " join fetch i.category c" +
                " where i.category.categoryId = :categoryId" +
                " order by i.itemId desc"; // 최신순으로 정렬 (itemId가 클수록 최신 데이터)

        // 추가적으로 정렬을 적용하는 경우
        if ("itemRatings".equals(sortBy)) {
            queryString += ", i.itemRatings desc";
        } else if ("likeCount".equals(sortBy)) {
            queryString += ", i.itemLike desc";
        } else if ("itemId".equals(sortBy)) {
            // 이미 최신순으로 정렬되어 있으므로 추가적인 정렬 필요 없음
        }

        TypedQuery<Item> query = em.createQuery(queryString, Item.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(offset)
                .setMaxResults(limit);

        return query.getResultList();
    }


    //검색기능
//    public List<Item> findByTitle(String keyword, Pageable pageable) {
//        return em.createQuery("select i from Item i where i.itemName like :keyword", Item.class)
//                .setParameter("keyword", "%" + keyword + "%")
//                .getResultList();
//    }







}
