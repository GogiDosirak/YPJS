package ypjs.project.dto.response;

import lombok.Getter;
import ypjs.project.domain.Item;

import java.util.List;

@Getter
public class ItemResponseDto {
    private Long categoryId;
    private Long itemId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;


    public ItemResponseDto() {}

    public ItemResponseDto(Long categoryId, Long itemId, String itemName, String itemContent, int itemPrice, int itemStock
                           ) {

        this.categoryId = categoryId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;

    }


}