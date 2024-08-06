package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
import ypjs.project.domain.Page;
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

    public String insert(@PathVariable("itemId") Long itemId, Model model, HttpSession session) {

        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");


        if(itemReviewService.existsByItemIdAndMemberId(itemId, responseLogin.getMemberId())) {
            ItemReview itemReview = itemReviewService.findItemReviewByItemIdAndMemberId(itemId, responseLogin.getMemberId());
            return "redirect:/ypjs/itemReview/getOne/" + itemReview.getItemReviewId();
        }


        Item findItem = itemService.findOneItem(itemId);

        model.addAttribute("item", findItem);


        return "itemreview/itemReviewPost";
    }


    //아이템 하나 조회
    @GetMapping("/ypjs/itemReview/getOne/{itemReviewId}")
    public String getOneItemReview(@PathVariable("itemReviewId") Long itemReviewId, HttpSession session, Model model) {
        ItemReview itemReview = itemReviewService.findOneItemReview(itemReviewId);

        ItemReviewListDto itemReviewListDto = new ItemReviewListDto(itemReview);

        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");
        model.addAttribute("itemReview",itemReviewListDto);
        model.addAttribute("memberId", responseLogin.getMemberId());

        return "itemreview/itemReviewGetOne";
    }





    //아이템 당 리뷰조회
    @GetMapping("/ypjs/itemReview/get/{itemId}")

    public String getItemReview(@PathVariable(name = "itemId") Long itemId, Model model, HttpSession session,

                                   @RequestParam(value = "page",defaultValue = "0") int page,
                                   @RequestParam(value = "sortBy", defaultValue = "itemReviewId") String sortBy,
                                   @RequestParam(value = "size",defaultValue = "10") int size) {


        Pageable pageable = PageRequest.of(page, size);

        itemService.findOneItem(itemId);

        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");



        List<ItemReviewListDto> itemReviews = itemReviewService.findItemReview(itemId, pageable, sortBy);


        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemReviewService.countAllItemReview(itemId), size);

        model.addAttribute("loginMemberId", responseLogin.getMemberId());

        model.addAttribute("itemReviews", itemReviews);
        model.addAttribute("sortBy", sortBy); // 정렬 옵션을 다시 모델에 추가
        model.addAttribute("page",page); //페이징
        model.addAttribute("size",size); //페이징
        model.addAttribute("totalPages", totalPages); //총 페이지 수



        return "itemreview/itemReviewGet";
    }




    //멤버 당 리뷰조회
    @GetMapping("/ypjs/itemReviewAll/get/{memberId}")
    public String getItemReviewByMember(@PathVariable(name = "memberId") Long memberId, Model model, HttpSession session,
                                @RequestParam(value = "page",defaultValue = "0") int page,
                                @RequestParam(value = "sortBy", defaultValue = "itemReviewId") String sortBy,
                                @RequestParam(value = "size",defaultValue = "10") int size) {


        Pageable pageable = PageRequest.of(page, size);

        memberService.findOne(memberId);

        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        List<ItemReviewListDto> itemReviews = itemReviewService.findItemReviewByMember(memberId, pageable, sortBy);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemReviewService.countAllItemReview(memberId), size);

        model.addAttribute("loginMemberId", responseLogin.getMemberId());

        model.addAttribute("itemReviews", itemReviews);
        model.addAttribute("sortBy", sortBy); // 정렬 옵션을 다시 모델에 추가
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

}
