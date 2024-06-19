package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
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
    public ItemReviewDto saveItemReview(@PathVariable("itemId") Long itemId, HttpSession session, Model model,
                                        @RequestBody @Valid ItemReviewDto requestDto) {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@"+itemId);
        Item findItem = itemService.findOneItem(itemId);
        //ItemReview itemReview = itemReviewService.saveItemReview(requestDto, responseLogin.getMemberId());
        ItemReview itemReview = itemReviewService.saveItemReview(requestDto, 1L);


        model.addAttribute("item", findItem);



        return new ItemReviewDto(itemReview.getItem().getItemId(),  itemReview.getItemScore(), itemReview.getItemReviewName(), itemReview.getItemReviewContent());
    }










    //아이템 당 리뷰조회
    @GetMapping("/ypjs/itemReview/get/{itemId}")
    public String getAllItemReview(@PathVariable("itemId") Long itemId, Model model) {

        itemService.findOneItem(itemId);

        List<ItemReviewListDto> itemReviews = itemReviewService.findAllItemReview(itemId);


        model.addAttribute("itemReviews", itemReviews);

        return "itemreview/itemReviewGet";
    }




    //수정
    @PutMapping("/ypjs/itemReview/update/{itemReviewId}")
    public ItemReviewDto updateItemReview(@PathVariable("itemReviewId") Long itemReviewId,
                                          @RequestBody @Valid ItemReviewDto itemReviewDto,
                                          HttpSession session) {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        itemReviewService.updateItemReview(itemReviewId, itemReviewDto);
        ItemReview findItemReview = itemReviewService.findOneItemReview(itemReviewId);

        return new ItemReviewDto(findItemReview.getItem().getItemId(),  findItemReview.getItemScore(), findItemReview.getItemReviewName(), findItemReview.getItemReviewContent());
    }


    //삭제
    @DeleteMapping("/ypjs/itemReview/delete/{itemReviewId}")
    public void deleteItemReview(@PathVariable("itemReviewId") Long itemReviewId){
        itemReviewService.deleteItemReview(itemReviewId);
    }

}
