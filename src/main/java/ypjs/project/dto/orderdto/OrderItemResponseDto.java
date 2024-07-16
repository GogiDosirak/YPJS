package ypjs.project.dto.orderdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.OrderItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long orderId;
    private Long itemId;
    private String itemName;
    private String itemFilepath;
    private int itemCount;
    private int itemTotalPrice;

    public static OrderItemResponseDto create (OrderItem oi) {
        OrderItemResponseDto oid = new OrderItemResponseDto();
        oid.orderId = oi.getOrder().getOrderId();
        oid.itemId = oi.getItem().getItemId();
        oid.itemName = oi.getItem().getItemName();
        oid.itemFilepath = oi.getItem().getItemFilepath();
        oid.itemCount = oi.getCount();
        oid.itemTotalPrice = oi.totalPrice();
        return oid;
    }

}
