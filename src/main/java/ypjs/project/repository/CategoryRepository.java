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
    public void saveCategory(Category category) {
        em.persist(category);

    }


    //categoryId 단건조회
    public Category findOneCategory(Long categoryId) {
        return em.find(Category.class, categoryId);
    }


    //categoryId 단건조회
//    public Category findOneCategory(Long categoryId) {
//        if (categoryId == null) {
//            throw new IllegalArgumentException("Category ID must not be null");
//        }
//        Category category = em.find(Category.class, categoryId);
//
//        if (category == null) {
//            throw new IllegalArgumentException("Category not found for ID: " + categoryId);
//        }
//        return category;
//    }


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



    //category전체 조회
    public List<Category> findAll() {
        return em.createQuery(
                        "select distinct c from Category c join fetch categoryParent", Category.class)
                .getResultList();
    }



    //카테고리 부모가 null일 때 카테고리 보이게
    public List<Category> findParentCategories() {
        return em.createQuery("select c from Category c where c.categoryParent is null", Category.class)
                .getResultList();
    }



}
