package ypjs.project.dto.itemdto;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemRequestDto {

    private Long categoryId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;


    public ItemRequestDto() {}

    public ItemRequestDto(Long categoryId, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;

    }
}
