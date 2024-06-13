package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.dto.CategoryListDto;
import ypjs.project.dto.CategoryRequestDto;
import ypjs.project.dto.CategoryUpdateDto;
import ypjs.project.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //category등록
//    @Transactional
//    public void saveCategory(Category category) {
//        categoryRepository.saveCategory(category);
//    }


    //category등록
    @Transactional
    public Category saveCategory(CategoryRequestDto categoryRequestDto) {
        Category parentCategory = categoryRepository.findOneCategory(categoryRequestDto.getCategoryParent());

        Category category = new Category(
                parentCategory,
                categoryRequestDto.getCategoryName()
        );

        categoryRepository.saveCategory(category);

        return category;
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
    public List<CategoryListDto> findAllCategory(CategoryListDto categoryListDto) {
      // List<Category> categories = categoryRepository.findAll();
       List<Category> categories = categoryRepository.findAllWithItem();

        List<CategoryListDto> result = categories.stream()
                .map(c -> new CategoryListDto(c))
                .collect(Collectors.toList());

        return result;
    }




    //category수정
    @Transactional
    public void updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryRepository.findOneCategory(categoryId);

        Category parentCategory = categoryRepository.findOneCategory(categoryUpdateDto.getCategoryParent());

        category.changeCategory(
                parentCategory,
                categoryUpdateDto.getCategoryName()
        );
    }


    //categtory삭제
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteCategory(categoryId);
    }



}
