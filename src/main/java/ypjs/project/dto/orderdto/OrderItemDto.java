package ypjs.project.dto.orderdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    //private String itemFilepath;
    private int itemCount;
    private int itemTotalPrice;

    public OrderItemDto(Long itemId, int count) {
        this.itemId = itemId;
        this.itemCount = count;
    }

}
