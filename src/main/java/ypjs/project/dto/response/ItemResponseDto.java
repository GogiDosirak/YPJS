package ypjs.project.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemResponseDto {
    private Long categoryId;
    private Long itemId;
    private String itemName;
    private String itemContent;
    private int price;
    private int stock;
   // private List<CategoryRespnseDto> cateroies;

    public ItemResponseDto() {}

    public ItemResponseDto(Long categoryId, Long itemId, String itemName, String itemContent, int price, int stock
                           ) {

        this.categoryId = categoryId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.price = price;
        this.stock = stock;
        //this.cateroies = cateroies;
    }
}