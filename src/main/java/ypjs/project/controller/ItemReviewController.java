package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.ItemReview;
import ypjs.project.dto.itemdto.ItemReviewDto;
import ypjs.project.service.ItemReviewService;
import ypjs.project.service.ItemService;

@RestController
@RequiredArgsConstructor
public class ItemReviewController {

    private final ItemReviewService itemReviewService;
    private final ItemService itemService;



    //리뷰등록
    @PostMapping("/ypjs/itemReview/post/{itemId}")
    public ItemReviewDto saveItemReview(@PathVariable("itemId") Long itemId,
                                        @RequestBody @Valid ItemReviewDto requestDto) {

        itemService.findOneItem(itemId);
        ItemReview itemReview = itemReviewService.saveItemReview(requestDto);

        return new ItemReviewDto(itemReview.getItem().getItemId(), itemReview.getMember().getMemberId(), itemReview.getItemScore(), itemReview.getItemReviewName(), itemReview.getItemReviewContent());
    }




    //수정
    @PutMapping("/ypjs/itemReview/update/{itemReviewId}")
    public ItemReviewDto updateItemReview(@PathVariable("itemReviewId") Long itemReviewId,
                                          @RequestBody @Valid ItemReviewDto itemReviewDto) {

        itemReviewService.updateItemReview(itemReviewId, itemReviewDto);
        ItemReview findItemReview = itemReviewService.findOneItemReview(itemReviewId);

        return new ItemReviewDto(findItemReview.getItem().getItemId(), findItemReview.getMember().getMemberId(), findItemReview.getItemScore(), findItemReview.getItemReviewName(), findItemReview.getItemReviewContent());
    }


    //삭제
    @DeleteMapping("/ypjs/itemReview/delete/{itemReviewId}")
    public void deleteItemReview(@PathVariable("itemReviewId") Long itemReviewId){
        itemReviewService.deleteItemReview(itemReviewId);
    }

}
