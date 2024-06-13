package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.*;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;


    //category등록
    @PostMapping("/ypjs/category/post")
    public CategoryRespnseDto saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDtorequest){

       Category category = categoryService.saveCategory(categoryRequestDtorequest);

        return new CategoryRespnseDto(category.getCategoryId(), category.getCategoryParent(), category.getCategoryName());
    }



    //categoryList보기
    @GetMapping("/ypjs/category/get")
    public List<CategoryListDto> getCategoryList(CategoryListDto categoryListDto){


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
