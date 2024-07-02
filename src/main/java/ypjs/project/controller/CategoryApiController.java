package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Page;
import ypjs.project.dto.categorydto.*;
import ypjs.project.dto.itemdto.ItemListDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;





    //category등록
    @PostMapping("/api/ypjs/category/post")
    public ResponseEntity saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDtorequest,
                                       HttpSession session){
        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");


        Category category = categoryService.saveCategory(categoryRequestDtorequest);

        return ResponseEntity.ok().build();

    }



    //category수정
    @ResponseBody
    @PutMapping("/api/ypjs/category/update/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable("categoryId") Long categoryId,
                                            @RequestBody @Valid CategoryUpdateDto categoryUpdateDto) {

        categoryService.updateCategory(categoryId, categoryUpdateDto);
        Category findCategory = categoryService.findOneCategory(categoryId);

        return ResponseEntity.ok().build();

   }




   //category삭제
    @DeleteMapping("/api/ypjs/category/delete/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }



}
