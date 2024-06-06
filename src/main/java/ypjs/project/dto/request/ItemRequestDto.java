package ypjs.project.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemRequestDto {

    private Long categoryId;
    private String itemName;
    private String itemContent;
    private int price;
    private int stock;
   // private List<CategoryRequestDto> cateroies;

    public ItemRequestDto() {}

    public ItemRequestDto(Long categoryId, String itemName, String itemContent, int price, int stock,
                          List<CategoryRequestDto> cateroies) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.price = price;
        this.stock = stock;
        //this.cateroies = cateroies;
    }
}
