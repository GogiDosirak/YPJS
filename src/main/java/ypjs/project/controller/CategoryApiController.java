package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.categorydto.*;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;





    //category등록
    @PostMapping("/api/ypjs/category/post")
    public ResponseEntity saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto,
                                       HttpSession session){
        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");


        categoryService.saveCategory(categoryRequestDto);

        return ResponseEntity.ok().build();

    }


    //    category수정
    @ResponseBody
    @PutMapping("/api/ypjs/category/update/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable("categoryId") Long categoryId,
                                         @RequestBody @Valid CategoryUpdateDto categoryUpdateDto) {

        categoryService.findOneCategory(categoryId);
        categoryService.updateCategory(categoryId,categoryUpdateDto);


        return ResponseEntity.ok().build();

    }





   //category삭제
    @DeleteMapping("/api/ypjs/category/delete/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }



}
