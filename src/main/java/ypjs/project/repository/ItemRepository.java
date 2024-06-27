package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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






    //카테고리 당 아이템 조회
    public List<Item> findAllItems(Long categoryId, Pageable pageable, String keyword) {
        String queryString = "select i from Item i join fetch i.category c where i.category.categoryId = :categoryId";

        // itemName 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            queryString += " and i.itemName like :itemName";
        }

        queryString += " order by i.itemId desc";

        TypedQuery<Item> query = em.createQuery(queryString, Item.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // itemName 파라미터 설정
        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("itemName", "%" + keyword + "%");
        }

        return query.getResultList();
    }







    //카테고리당 아이템리스트
    public List<Item> findAllItemPagingSortByAndKeyword(Long categoryId, String keyword, Pageable pageable, String sortBy) {
        String queryString = "select distinct i from Item i" +
                " join fetch i.category c" +
                " where i.category.categoryId = :categoryId";

        // 검색 조건 추가
        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
        if (hasKeyword) {
            queryString += " and i.itemName like :keyword";
        }


        queryString += " order by "; // 기본 order by 절을 먼저 추가
        switch (sortBy) {
            case "itemRatings":
                queryString += "i.itemRatings desc, i.itemId desc"; // itemRatings로 정렬, 동일한 rating이면 itemId로 정렬
                break;
            case "likeCount":
                queryString += "i.likeCount desc, i.itemId desc"; // likeCount로 정렬, 동일한 likeCount이면 itemId로 정렬
                break;
            case "itemId":
            default:
                queryString += "i.itemId desc"; // 기본적으로 itemId로 정렬
                break;
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


    //아이템 전체리스트
    public List<Item> findAllItem(String keyword, Pageable pageable, String sortBy) {
        // 기본 쿼리
        String queryString = "select i from Item i";

        // 검색 조건 추가
        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
        if (hasKeyword) {
            queryString += " where i.itemName like :keyword";
        }

        // 정렬 조건
        queryString += " order by ";
        switch (sortBy) {
            case "itemRatings":
                queryString += "i.itemRatings desc, i.itemId desc";
                break;
            case "likeCount":
                queryString += "i.likeCount desc, i.itemId desc";
                break;
            case "itemId":
            default:
                queryString += "i.itemId desc";
                break;
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



    // 아이템 전체 총 개수 조회 (페이징 하기 위해)
    public int countAll(String keyword) {
        String queryString = "select count(i) from Item i";
        if (keyword != null && !keyword.isEmpty()) {
            queryString += " where i.itemName like :keyword";
        }
        TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        return query.getSingleResult().intValue();
    }


    //카테고리 별 아이템 총 개수 조회(페이징)
    public int countAllCategoryItem(String keyword, Long categoryId) {
        // 기본 쿼리 설정
        String queryString = "select count(distinct i) from Item i join i.category c where i.category.categoryId = :categoryId";

        // 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            queryString += " and i.itemName like :keyword";
        }

        // 쿼리 생성
        TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("categoryId", categoryId);

        // 검색 키워드 파라미터 설정
        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        // 결과 반환
        return query.getSingleResult().intValue();
    }




    //검색기능
//    public List<Item> findByTitle(String keyword) {
//        return em.createQuery("select i from Item i where i.itemName like :keyword", Item.class)
//                .setParameter("keyword", "%" + keyword + "%")
//                .getResultList();
//    }







}
