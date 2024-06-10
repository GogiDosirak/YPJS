package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ypjs.project.domain.Item;

@Getter
public class ItemOneDto {

    private String itemName;
    private String itemContent;
    private int itemPrice;
    private int itemStock;


    public ItemOneDto() {}

    public ItemOneDto(Item item) {


        itemName = item.getItemName();
        itemContent = item.getItemContent();
        itemPrice = item.getItemPrice();
        itemStock = item.getItemStock();

    }
}
