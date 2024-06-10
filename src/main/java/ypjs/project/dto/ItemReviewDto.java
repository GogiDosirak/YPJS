package ypjs.project.dto;

import lombok.Getter;

@Getter
public class ItemReviewDto {

    private Long itemId;
    private Long memberId;
    private Double itemScore;
    private String itemReviewName;
    private String itemReviewContent;


    public ItemReviewDto() {}


    public ItemReviewDto(Long itemId, Long memberId, Double itemScore, String itemReviewName, String itemReviewContent) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
    }




}
