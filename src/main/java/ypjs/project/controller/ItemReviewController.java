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
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
import ypjs.project.domain.Page;
import ypjs.project.dto.itemdto.ItemOneDto;
import ypjs.project.dto.itemdto.ItemReviewDto;
import ypjs.project.dto.itemdto.ItemReviewListDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.ItemReviewService;
import ypjs.project.service.ItemService;
import ypjs.project.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemReviewController {

    private final ItemReviewService itemReviewService;
    private final ItemService itemService;
    private final MemberService memberService;


    //리뷰등록 화면
    @GetMapping("/ypjs/itemReview/post/{itemId}")
    public String insert(@PathVariable("itemId") Long itemId, Model model) {

        Item findItem = itemService.findOneItem(itemId);

        model.addAttribute("item", findItem);

        return "itemreview/itemReviewPost";}


   //리뷰등록
    @ResponseBody
    @PostMapping("/ypjs/itemReview/post/{itemId}")
    public ResponseEntity saveItemReview(@PathVariable("itemId") Long itemId, HttpSession session, Model model,
                                        @RequestBody @Valid ItemReviewDto requestDto) {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        Item findItem = itemService.findOneItem(itemId);
        //ItemReview itemReview = itemReviewService.saveItemReview(requestDto, responseLogin.getMemberId());
        ItemReview itemReview = itemReviewService.saveItemReview(requestDto, 1L);


        model.addAttribute("item", findItem);


        return ResponseEntity.ok().build();
    }








    //아이템 당 리뷰조회
    @GetMapping("/ypjs/itemReview/get/{itemId}")
    public String getAllItemReview(@PathVariable(name = "itemId") Long itemId, Model model,
                                   @RequestParam(value = "page",defaultValue = "0") int page,
                                   @RequestParam(value = "size",defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        itemService.findOneItem(itemId);

        List<ItemReviewListDto> itemReviews = itemReviewService.findAllItemReview(itemId, pageable);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemReviewService.countAllItemReview(itemId), size);

        model.addAttribute("itemReviews", itemReviews);
        model.addAttribute("page",page); //페이징
        model.addAttribute("size",size); //페이징
        model.addAttribute("totalPages", totalPages); //총 페이지 수


        return "itemreview/itemReviewGet";
    }





    // 수정보기
    @GetMapping("/ypjs/itemReview/update/{itemReviewId}")
    public String updateItemReview(@PathVariable("itemReviewId") Long itemReviewId, Model model) {
        ItemReview findItemReview = itemReviewService.findOneItemReview(itemReviewId);

        ItemReviewDto itemReview = new ItemReviewDto(
                findItemReview.getItem().getItemId(),
                findItemReview.getItemReviewId(),
                findItemReview.getItemScore(),
                findItemReview.getItemReviewName(),
                findItemReview.getItemReviewContent()

        );



        model.addAttribute("itemReview", itemReview);

        // 반환할 뷰 이름 (템플릿 파일 경로, 예: templates/itemReview/update.html)
        return "itemReview/itemReviewUpdate";
    }



    //수정등록
    @ResponseBody
    @PutMapping("/ypjs/itemReview/update/{itemReviewId}")
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
    @DeleteMapping("/ypjs/itemReview/delete/{itemReviewId}")
    public ResponseEntity deleteItemReview(@PathVariable("itemReviewId") Long itemReviewId){
        itemReviewService.deleteItemReview(itemReviewId);

        return ResponseEntity.ok().build();
    }












}
