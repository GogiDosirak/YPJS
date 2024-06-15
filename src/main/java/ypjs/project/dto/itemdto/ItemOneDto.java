package ypjs.project.dto.itemdto;

import lombok.Getter;
import ypjs.project.domain.Item;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemOneDto {

    private String itemName;
    private String itemContent;
    private int itemPrice;
    private double itemRatings;

    private List<ItemReviewListDto> itemReviews;


    public ItemOneDto() {
    }

    public ItemOneDto(Item item) {


        itemName = item.getItemName();
        itemContent = item.getItemContent();
        itemPrice = item.getItemPrice();
        itemRatings = item.getItemRatings();

        itemReviews = item.getItemReviews().stream()
                .map(itemReview -> new ItemReviewListDto(itemReview))
                .collect(Collectors.toList());

    }


}







