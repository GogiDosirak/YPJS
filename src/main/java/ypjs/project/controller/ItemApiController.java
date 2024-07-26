package ypjs.project.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ypjs.project.domain.Item;
import ypjs.project.dto.itemdto.*;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.ItemService;


@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;




    //item등록
    @PostMapping("/api/ypjs/item/post")
    public void saveItem(@RequestParam("file") MultipartFile file, Model model,
                           @Valid @ModelAttribute  ItemRequestDto requestDto,
                           @Valid @ModelAttribute  ItemFileDto itemFileDto,
                           HttpSession session, HttpServletResponse response) throws Exception {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

       Item memberId = itemService.saveItem(requestDto, responseLogin.getMemberId(), itemFileDto, file);
       model.addAttribute("memberId", memberId);

        //멤버 임시로 넣어 놈
       // itemService.saveItem(requestDto, 1L, itemFileDto, file);

        response.sendRedirect("/ypjs/item/get");


    }





    //수정등록
    @PostMapping("/api/ypjs/item/update/{itemId}")
    public void updateItem(@PathVariable("itemId") Long itemId,
                           @RequestParam("file") MultipartFile file,
                           @Valid @ModelAttribute  ItemUpdateDto itemUpdateDto,
                           @Valid @ModelAttribute  ItemFileDto itemFileDto, Model model,
                           HttpSession session, HttpServletResponse response) throws Exception {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        itemService.findOneItem(itemId);
        itemService.updateItem(itemId, itemUpdateDto, itemFileDto, file);


        response.sendRedirect("/ypjs/item/get/" + itemId);


    }


    //삭제
    @DeleteMapping("/api/ypjs/item/delete/{itemId}")
    public ResponseEntity deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }




}