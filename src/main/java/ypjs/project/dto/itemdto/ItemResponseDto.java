package ypjs.project.dto.itemdto;

import lombok.Getter;
import ypjs.project.domain.Item;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
public class ItemResponseDto {
    private Long categoryId;
    private Long itemId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;
    private LocalDateTime itemCreateDate;


    public ItemResponseDto() {}

    public ItemResponseDto(Long categoryId, Long itemId, String itemName, String itemContent, int itemPrice, int itemStock,
                           LocalDateTime itemCreateDate
                           ) {

        this.categoryId = categoryId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemCreateDate = itemCreateDate;

    }


}