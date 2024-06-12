package ypjs.project.dto;

import lombok.Getter;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemReviewListDto {

    Long itemId;
    // String nickname;
    private int itemScore;
    private String itemReviewName;
    private String itemReviewContent;


    public ItemReviewListDto() {}

    public ItemReviewListDto(ItemReview itemReview) {

        itemId = itemReview.getItem().getItemId();
        // nickname = itemReview.getMember().getNickname();
        itemScore = itemReview.getItemScore();
        itemReviewName = itemReview.getItemReviewName();
        itemReviewContent = itemReview.getItemReviewContent();


    }





}
