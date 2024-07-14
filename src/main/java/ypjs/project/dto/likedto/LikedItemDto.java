package ypjs.project.dto.likedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Item;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikedItemDto {

    private String itemName;
    private int itemPrice;
    private String itemFilename;
    private String itemFilepath;

    public LikedItemDto(Item item){
        itemName = item.getItemName();
        itemPrice = item.getItemPrice();
        itemFilename = item.getItemFilename();
        itemFilepath = getItemFilepath();
    }

}
