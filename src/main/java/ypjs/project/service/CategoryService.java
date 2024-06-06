package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //category등록
    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.saveCategory(category);
    }


    //categoryId단건조회
    public Category findOneCategory(Long categoryId) {
        return categoryRepository.findOneCategory(categoryId);
    }

    //categoryParentId조회
    public List<Category> findCategoryParent(Category categoryParent) {
        return categoryRepository.findByParentId(categoryParent);
    }


    //category전체 조회
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

}
