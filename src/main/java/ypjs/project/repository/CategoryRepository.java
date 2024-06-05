package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Category;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {


    private final EntityManager em;


    //카테고리 저장
    public void saveCategory(Category  category) {
        if(category.getCategoryId() == null) {
            em.persist(category);
        } else {
            em.merge(category);
        }
    }

    //CategoryParentId통해서 조회
    public List<Category> findByParentId(Category categoryParent) {
        return em.createQuery("select c from Category c where c.categoryParent = :categoryParent", Category.class)
                .setParameter("categoryParent", categoryParent)
                .getResultList();
    }


    //category전체 조회
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }







}
