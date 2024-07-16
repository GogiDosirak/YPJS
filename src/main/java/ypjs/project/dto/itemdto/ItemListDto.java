package ypjs.project.dto.itemdto;

import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Item;

@Data
public class ItemListDto {

    private Long itemId;
    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;
    private double itemRatings;
    private String itemFilePath;
    private int itemCnt;

    public ItemListDto() {}

    public ItemListDto(Item item) {

        itemId = item.getItemId();
        itemName = item.getItemName();
        itemContent = item.getItemContent();
        itemPrice = item.getItemPrice();
        itemStock = item.getItemStock();
        itemRatings = item.getItemRatings();
        itemFilePath = item.getItemFilepath();
        itemCnt = item.getItemCnt();

    }




}
