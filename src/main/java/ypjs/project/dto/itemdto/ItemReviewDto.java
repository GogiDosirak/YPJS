package ypjs.project.dto.itemdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class ItemReviewDto {

    private Long itemId;

    private Long itemReviewId;

    @Min(value = 1, message = "itemScore must be at least 1 or greater.")
    private int itemScore;

    @NotBlank(message = "itemReviewName not be null")
    private String itemReviewName;

    @NotBlank(message = "itemReviewContent not be null")
    private String itemReviewContent;


    public ItemReviewDto() {}


    public ItemReviewDto(Long itemId, Long itemReviewId,int itemScore, String itemReviewName, String itemReviewContent) {
        this.itemId = itemId;
        this.itemReviewId = itemReviewId;
        this.itemScore = itemScore;
        this.itemReviewName = itemReviewName;
        this.itemReviewContent = itemReviewContent;
    }




}
