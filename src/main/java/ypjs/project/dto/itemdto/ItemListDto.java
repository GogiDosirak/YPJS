package ypjs.project.dto.itemdto;

import lombok.Getter;
import ypjs.project.domain.Item;

@Getter
public class ItemListDto {

    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;

    public ItemListDto() {}

    public ItemListDto(Item item) {

        itemName = item.getItemName();
        itemContent = item.getItemContent();
        itemPrice = item.getItemPrice();
        itemStock = item.getItemStock();

    }




}
