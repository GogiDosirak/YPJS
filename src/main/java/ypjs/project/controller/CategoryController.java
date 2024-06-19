package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.categorydto.*;
import ypjs.project.dto.itemdto.ItemListDto;
import ypjs.project.dto.itemdto.ItemOneDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.repository.CategoryRepository;
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



    //category등록
    @PostMapping("/ypjs/category/post")
    public CategoryRespnseDto saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDtorequest,
                                           HttpSession session){
        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");


        Category category = categoryService.saveCategory(categoryRequestDtorequest);

        return new CategoryRespnseDto(category.getCategoryId(), category.getCategoryParent(), category.getCategoryName());
    }






    //category 1개 조회
    @GetMapping("/ypjs/category/get/{categoryId}")
    public String getOneCategory (@PathVariable("categoryId") Long categoryId,
                                   Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size) {


        Pageable pageable = PageRequest.of(page, size);

        Category findCategory = categoryService.findOneCategory(categoryId);

        List<ItemListDto> items = itemService.findAllCategoryItem(categoryId, pageable);

//        CategoryOneDto category = new CategoryOneDto( findCategory, items);

        model.addAttribute("category", findCategory);
        model.addAttribute("items",items);

        return "category/categoryGet";


    }




    //categoryList보기
    @GetMapping("/ypjs/category/get")
    public String getCategoryList(Model model,
                                  @RequestParam(value = "categoryId", required = false) Long categoryId,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "8") int size,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);

        List<CategoryListDto> categories = categoryService.findAllCategory(categoryId, keyword, pageable);

        model.addAttribute("categories", categories);

        return "category/categoryList";
    }






    //카테고리수정보기
    @GetMapping("/ypjs/category/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Long categoryId, Model model) {

        Category findCategory = categoryService.findOneCategory(categoryId);

        CategoryUpdateDto category = new CategoryUpdateDto(findCategory.getCategoryId(),
                findCategory.getCategoryParent().getCategoryId(),
                findCategory.getCategoryName());

        model.addAttribute("category", category);
        return "category/categoryUpdate";
    }




    //category수정
    @ResponseBody
    @PutMapping("ypjs/category/update/{categoryId}")
    public CategoryUpdateDto updateCategory(@PathVariable("categoryId") Long categoryId,
                                            @RequestBody @Valid CategoryUpdateDto categoryUpdateDto) {

        categoryService.updateCategory(categoryId, categoryUpdateDto);
        Category findCategory = categoryService.findOneCategory(categoryId);


       return new CategoryUpdateDto(findCategory.getCategoryId(), findCategory.getCategoryParent().getCategoryId(), findCategory.getCategoryName());
   }


   //category삭제
    @DeleteMapping("ypjs/category/delete/{categoryId}")
    public @ResponseBody ResponseDto<?> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseDto<>(HttpStatus.OK.value(), "카테고리가 삭제되었습니다.");
    }















}
