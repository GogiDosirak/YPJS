package ypjs.project.dto.cartdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long cartId;
    private Long itemId;
    private String itemName;
    //private String itemFilepath;
    private int itemCount;
    private int itemTotalPrice;

}
