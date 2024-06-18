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

    private int itemCount;

    private int itemTotalPrice;

}
