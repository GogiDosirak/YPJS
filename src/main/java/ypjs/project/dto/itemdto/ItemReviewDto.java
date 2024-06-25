package ypjs.project.dto.itemdto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class ItemReviewDto {

    private Long itemId;
    private Long itemReviewId;
    private int itemScore;
    private String itemReviewName;
    private String itemReviewContent;


    public ItemReviewDto() {}


    public ItemReviewDto(Long itemId, Long itemReviewId, int itemScore, String itemReviewName, String itemReviewContent) {
        this.itemId = itemId;
        this.itemReviewId = itemReviewId;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
    }




}
