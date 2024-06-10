package ypjs.project.dto;

import lombok.Getter;

@Getter
public class ItemUpdateDto {
    private Long categoryId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;

    public ItemUpdateDto(){}

    public ItemUpdateDto(Long categoryId, Long itemId, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
    }
}
