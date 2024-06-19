package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {


    private final EntityManager em;


    //카테고리 저장
    public void saveCategory(Category  category) {
            em.persist(category);

    }


    //categoryId 단건조회
//    public Category findOneCategory(Long categoryId) {
//        return em.find(Category.class, categoryId);
//    }


    //categoryId 단건조회
    public Category findOneCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }
        Category category = em.find(Category.class, categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found for ID: " + categoryId);
        }
        return category;
    }



    //CategoryParentId통해서 조회
    public List<Category> findByParentId(Category categoryParent) {
        return em.createQuery("select c from Category c where c.categoryParent = :categoryParent", Category.class)
                .setParameter("categoryParent", categoryParent)
                .getResultList();
    }


    //category삭제
    public void deleteCategory(Long categoryId) {
        Category findCategory = em.find(Category.class, categoryId);
        em.remove(findCategory);
    }



    // category 전체 조회
    public List<Category> findAll(Long categoryId, String keyword, Pageable pageable) {
        String queryString = "select c from Category c where 1=1";

        // 카테고리 아이디로 검색 조건 추가
        if (categoryId != null) {
            queryString += " and c.categoryId = :categoryId";
        }

        // 키워드로 카테고리 이름 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            queryString += " and c.categoryName like :keyword";
        }

        // 카테고리 아이디와 키워드가 모두 비어 있는 경우
        if (categoryId == null && (keyword == null || keyword.isEmpty())) {
            // 이 경우, 전체 카테고리를 조회하도록 queryString에 추가 조건 없음
            queryString = "select c from Category c";
        }

        TypedQuery<Category> query = em.createQuery(queryString, Category.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // 매개변수 바인딩
        if (categoryId != null) {
            query.setParameter("categoryId", categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        return query.getResultList();
    }









    //categoryId 전체 조회(페치조인, 이거 하면 아이템, 카테고리 페어런츠가 있는 것들만 리스트로 나옴)
//    public List<Category> findAllWithItem() {
//        return em.createQuery(
//                        "select distinct c from Category c" +
//                                " join fetch c.items i" +
//                                " join fetch c.categoryParent cp" +
//                                " order by c.categoryId desc",
//                        Category.class)
//                .getResultList();
//    }











}
