package ypjs.project.dto.itemdto;

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
