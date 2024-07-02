package ypjs.project.dto.itemdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class ItemUpdateDto {
    private Long itemId;
    @NotNull(message = "Category ID not be null")
    private Long categoryId;

    @NotBlank(message = "ItemName not be null")
    private String itemName;
    @NotBlank(message = "ItemContent not be null")
    private String itemContent;

    @Min(value = 1, message = "ItemPrice must be at least 1 or greater.")
    private int itemPrice;

    @Min(value = 1, message = "ItemStock must be at least 1 or greater.")
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
