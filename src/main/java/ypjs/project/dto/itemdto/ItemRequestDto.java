package ypjs.project.dto.itemdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ypjs.project.domain.Member;

import java.util.List;

@Data
public class ItemRequestDto {

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



    public ItemRequestDto() {}

    public ItemRequestDto(Long categoryId, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;


    }





}
