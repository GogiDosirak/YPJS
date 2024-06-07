package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.domain.Category;
import ypjs.project.dto.request.CategoryRequestDto;
import ypjs.project.dto.response.CategoryDto;
import ypjs.project.dto.response.CategoryRespnseDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/ypjs/category/post")
    public CategoryRespnseDto saveCategory(@RequestBody @Valid CategoryRequestDto request){
        Category parentCategory = categoryService.findOneCategory(request.getCategoryParent());

        //System.out.println(parentCategory);

        Category category = new Category(
                parentCategory,
                request.getCategoryName()
        );

        //System.out.println(category);

        categoryService.saveCategory(category);

        return new CategoryRespnseDto(category.getCategoryId(), category.getCategoryParent(), category.getCategoryName());
    }


    //categoryList보기
    @GetMapping("/ypjs/category/get")
    public List<CategoryDto> getCategoryList() {

        List<Category> categories = categoryService.findAllCategory();
        //List<Category> categories = categoryRepository.findAllWithItem();


        List<CategoryDto> result = categories.stream()
                .map(c -> new CategoryDto(c))
                .collect(Collectors.toList());

        return result;
    }

















}
