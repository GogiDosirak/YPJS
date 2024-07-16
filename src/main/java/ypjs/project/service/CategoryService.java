package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.dto.categorydto.CategoryListDto;
import ypjs.project.dto.categorydto.CategoryRequestDto;
import ypjs.project.dto.categorydto.CategoryUpdateDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.repository.ItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

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

        parentCategory.addChildCategory(category);

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
    public List<CategoryListDto> findAllCategory() {

        List<Category> categories = categoryRepository.findAll();


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




    //카테고리 부모가 null일 때 카테고리 보이게
    public List<Category> findParentCategories() {
        return categoryRepository.findParentCategories();
    }

}
