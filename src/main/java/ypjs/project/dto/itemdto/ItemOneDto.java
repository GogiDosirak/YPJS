package ypjs.project.dto.itemdto;

import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemOneDto {

    private Long itemId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private double itemRatings;
    private LocalDateTime itemCreateDate;
    private int itemCnt;

    private List<ItemReviewListDto> itemReviews;


    public ItemOneDto(Long itemId, String itemName, String itemContent, int itemPrice, Double itemRatings,
                      LocalDateTime itemCreateDate, int itemCnt, List<ItemReview> itemReviews) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemRatings = itemRatings;
        this.itemCreateDate = itemCreateDate;
        this.itemCnt = itemCnt;

        this.itemReviews = itemReviews.stream()
                .map(ItemReviewListDto::new)
                .collect(Collectors.toList());
    }


    public ItemOneDto(Item item) {
        this.itemId = item.getItemId();
        this.itemName = item.getItemName();
        this.itemContent = item.getItemContent();
        this.itemPrice = item.getItemPrice();
        this.itemRatings = item.getItemRatings();
        this.itemCreateDate = item.getItemCreateDate();
        this.itemCnt = item.getItemCnt();

        this.itemReviews = item.getItemReviews().stream()
                .map(ItemReviewListDto::new)
                .collect(Collectors.toList());
    }
}
