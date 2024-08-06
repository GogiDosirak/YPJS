package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
import ypjs.project.dto.itemdto.ItemReviewDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.ItemReviewService;
import ypjs.project.service.ItemService;
import ypjs.project.service.MemberService;

@Controller
@RequiredArgsConstructor
public class ItemReviewApiController {

    private final ItemReviewService itemReviewService;
    private final ItemService itemService;
    private final MemberService memberService;





   //리뷰등록
    @ResponseBody
    @PostMapping("/api/ypjs/itemReview/post/{itemId}")
    public ResponseEntity saveItemReview(@PathVariable("itemId") Long itemId, HttpSession session, Model model,
                                        @RequestBody @Valid ItemReviewDto requestDto) {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");



        Item findItem = itemService.findOneItem(itemId);
        ItemReview memberId = itemReviewService.saveItemReview(requestDto, responseLogin.getMemberId());



        model.addAttribute("memberId", memberId);
        model.addAttribute("item", findItem);


        return ResponseEntity.ok().build();
    }


    //수정등록
    @ResponseBody
    @PutMapping("/api/ypjs/itemReview/update/{itemReviewId}")
    public ResponseEntity updateItemReview(@PathVariable(name ="itemReviewId") Long itemReviewId,
                                           @RequestBody @Valid ItemReviewDto itemReviewDto,
                                           HttpSession session) {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");


        itemReviewService.updateItemReview(itemReviewId, itemReviewDto);
        ItemReview findItemReview = itemReviewService.findOneItemReview(itemReviewId);

        return ResponseEntity.ok().body(findItemReview.getItem().getItemId());

    }




    //삭제
    @DeleteMapping("/api/ypjs/itemReview/delete/{itemReviewId}")
    public ResponseEntity deleteItemReview(@PathVariable("itemReviewId") Long itemReviewId){
        itemReviewService.deleteItemReview(itemReviewId);

        return ResponseEntity.ok().build();
    }





}
