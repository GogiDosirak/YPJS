package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.dto.CategoryRequestDto;
import ypjs.project.dto.CategoryListDto;
import ypjs.project.dto.CategoryRespnseDto;
import ypjs.project.dto.CategoryUpdateDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;


    //category등록
    @PostMapping("/ypjs/category/post")
    public CategoryRespnseDto saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDtorequest){

       Category category = categoryService.saveCategory(categoryRequestDtorequest);

        return new CategoryRespnseDto(category.getCategoryId(), category.getCategoryParent(), category.getCategoryName());
    }


    //categoryList보기
    @GetMapping("/ypjs/category/get")
    public List<CategoryListDto> getCategoryList(CategoryListDto categoryListDto) {

        return categoryService.findAllCategory(categoryListDto);

    }



    //category수정
   @PutMapping("ypjs/category/update/{categoryId}")
    public CategoryUpdateDto updateCategory(@PathVariable("categoryId") Long categoryId,
                               @RequestBody @Valid CategoryUpdateDto categoryUpdateDto) {

        categoryService.updateCategory(categoryId, categoryUpdateDto);
        Category findCategory = categoryService.findOneCategory(categoryId);


       return new CategoryUpdateDto(findCategory.getCategoryId(), findCategory.getCategoryParent().getCategoryId(), findCategory.getCategoryName());
   }


   //category삭제
    @DeleteMapping("ypjs/category/delete/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
















}
