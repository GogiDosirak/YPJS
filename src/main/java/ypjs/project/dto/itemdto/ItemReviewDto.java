package ypjs.project.dto.itemdto;

import lombok.Getter;

@Getter
public class ItemReviewDto {

    private Long itemId;
    private Long memberId;
    private int itemScore;
    private String itemReviewName;
    private String itemReviewContent;


    public ItemReviewDto() {}


    public ItemReviewDto(Long itemId, Long memberId, int itemScore, String itemReviewName, String itemReviewContent) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
    }




}
