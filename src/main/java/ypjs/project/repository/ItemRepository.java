package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;




    //상품저장
    public void saveItem(Item item) {
            em.persist(item);

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




    //카테고리 당 아이템 조회 기본 최신순 정렬, 후기 많은 순, 좋아요 많은 순 추가 정렬 (영한아저씨 따라한 것)
//    public List<Item> findAllItemSortBy(Long categoryId, int offset, int limit, String sortBy) {
//        String queryString = "select distinct i from Item i" +
//                " join fetch i.category c" +
//                " where i.category.categoryId = :categoryId" +
//                " order by i.itemId desc"; // 최신순으로 정렬 (itemId가 클수록 최신 데이터)
//
//        // 추가적으로 정렬을 적용하는 경우
//        if ("itemRatings".equals(sortBy)) {
//            queryString += ", i.itemRatings desc";
//        } else if ("likeCount".equals(sortBy)) {
//            queryString += ", i.itemLike desc";
//        } else if ("itemId".equals(sortBy)) {
//            // 이미 최신순으로 정렬되어 있으므로 추가적인 정렬 필요 없음
//        }
//
//        TypedQuery<Item> query = em.createQuery(queryString, Item.class)
//                .setParameter("categoryId", categoryId)
//                .setFirstResult(offset)
//                .setMaxResults(limit);
//
//        return query.getResultList();
//    }




    //카테고리 당 아이템 조회(검색, 정렬, 페이징)
    public List<Item> findAllItemPagingSortByAndKeyword(Long categoryId, String keyword, Pageable pageable, String sortBy) {
        String queryString = "select distinct i from Item i" +
                " join fetch i.category c" +
                " where i.category.categoryId = :categoryId";

        // 검색 조건 추가
        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
        if (hasKeyword) {
            queryString += " and i.itemName like :keyword";
        }

        // 기본 정렬 조건
        queryString += " order by i.itemId desc"; // 최신순으로 정렬 (itemId가 클수록 최신 데이터)

        // 추가 정렬 조건
        if ("itemRatings".equals(sortBy)) {
            queryString += ", i.itemRatings desc";
        } else if ("likeCount".equals(sortBy)) {
            queryString += ", i.likeCount desc";
        }

        TypedQuery<Item> query = em.createQuery(queryString, Item.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // 검색 키워드 파라미터 설정
        if (hasKeyword) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        return query.getResultList();
    }


    //아이템 전체 조회
    public List<Item> findAllItem(String keyword, Pageable pageable, String sortBy) {
        String queryString = "select i from Item i";

        // 검색 조건 추가
        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
        if (hasKeyword) {
            queryString += " where i.itemName like :keyword";
        }

        // 기본 정렬 조건
        queryString += " order by i.itemId desc"; // 최신순으로 정렬 (itemId가 클수록 최신 데이터)

        // 추가 정렬 조건
        if ("itemRatings".equals(sortBy)) {
            queryString += ", i.itemRatings desc";
        } else if ("likeCount".equals(sortBy)) {
            queryString += ", i.likeCount desc";
        }

        TypedQuery<Item> query = em.createQuery(queryString, Item.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // 검색 키워드 파라미터 설정
        if (hasKeyword) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        return query.getResultList();
    }


//    public List<Item> findAllItem(String keyword, String sortBy) {
//        String queryString = "select i from Item i";
//
//        // 검색 조건 추가
//        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
//        if (hasKeyword) {
//            queryString += " where i.itemName like :keyword";
//        }
//
//        // 기본 정렬 조건
//        queryString += " order by i.itemId desc"; // 최신순으로 정렬 (itemId가 클수록 최신 데이터)
//
//        // 추가 정렬 조건
//        if ("itemRatings".equals(sortBy)) {
//            queryString += ", i.itemRatings desc";
//        } else if ("likeCount".equals(sortBy)) {
//            queryString += ", i.likeCount desc";
//        }
//
//        TypedQuery<Item> query = em.createQuery(queryString, Item.class);
//
//        // 검색 키워드 파라미터 설정
//        if (hasKeyword) {
//            query.setParameter("keyword", "%" + keyword + "%");
//        }
//
//        return query.getResultList();
//    }


    //검색기능
//    public List<Item> findByTitle(String keyword) {
//        return em.createQuery("select i from Item i where i.itemName like :keyword", Item.class)
//                .setParameter("keyword", "%" + keyword + "%")
//                .getResultList();
//    }







}
