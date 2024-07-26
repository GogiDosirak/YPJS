package ypjs.project.dto.itemdto;

import lombok.Getter;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemReviewListDto {

    Long itemReviewId;
    Long itemId;
    String nickname;
    Long memberId;
    private int itemScore;
    private String itemReviewName;
    private String itemReviewContent;
    private LocalDateTime itemReviewCreateDate;



    public ItemReviewListDto() {}

    public ItemReviewListDto(ItemReview itemReview) {

        itemReviewId = itemReview.getItemReviewId();
        itemId = itemReview.getItem().getItemId();
        nickname = itemReview.getMember().getNickname();
        memberId = itemReview.getMember().getMemberId();
        itemScore = itemReview.getItemScore();
        itemReviewName = itemReview.getItemReviewName();
        itemReviewContent = itemReview.getItemReviewContent();
        itemReviewCreateDate = itemReview.getItemReviewCreateDate();


    }



}
