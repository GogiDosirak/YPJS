package ypjs.project.dto.itemdto;

import lombok.Data;
import lombok.Getter;

@Data
public class ItemUpdateDto {
    private Long itemId;
    private Long categoryId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;



    public ItemUpdateDto(){}

    public ItemUpdateDto(Long itemId,Long categoryId, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;

    }
}
