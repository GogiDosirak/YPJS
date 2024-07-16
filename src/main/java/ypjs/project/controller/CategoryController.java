package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.domain.Category;
import ypjs.project.domain.Page;
import ypjs.project.dto.categorydto.CategoryListDto;
import ypjs.project.dto.categorydto.CategoryUpdateDto;
import ypjs.project.dto.itemdto.ItemListDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;
    private final ItemService itemService;


    //카테고리 등록 화면
    @GetMapping("/ypjs/category/post")
    public String insert() {return "category/categoryPost";}



    //category 1개 조회
    @GetMapping("/ypjs/category/get/{categoryId}")
    public String getOneCategory (@PathVariable("categoryId") Long categoryId,
                                  Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                  @RequestParam(value = "keyword", required = false) String keyword) {


        Pageable pageable = PageRequest.of(page, size);

        Category findCategory = categoryService.findOneCategory(categoryId);

        List<ItemListDto> items = itemService.findAllCategoryItem(categoryId, pageable, keyword);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemService.countAllCategoryItem(keyword, categoryId), size);


        model.addAttribute("category", findCategory);
        model.addAttribute("items",items);
        model.addAttribute("keyword", keyword); //검색조건 유지
        model.addAttribute("page",page); //페이징
        model.addAttribute("size",size); //페이징
        model.addAttribute("totalPages", totalPages); //총 페이지 수


        return "category/categoryGet";


    }




    //카테고리 리스트 보기
    @GetMapping("/ypjs/category/get")
    public String getCategoryList(Model model) {

        List<CategoryListDto> categories = categoryService.findAllCategory();

        model.addAttribute("categories", categories);

        return "category/categoryList";
    }



    //카테고리수정보기
    @GetMapping("/ypjs/category/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Long categoryId, Model model) {

        Category findCategory = categoryService.findOneCategory(categoryId);

        CategoryUpdateDto category = new CategoryUpdateDto(
                findCategory.getCategoryId(),
                findCategory.getCategoryParent().getCategoryId(),
                findCategory.getCategoryName());

        model.addAttribute("category", category);
        return "category/categoryUpdate";
    }



}
