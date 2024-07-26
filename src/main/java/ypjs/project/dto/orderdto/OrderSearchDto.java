package ypjs.project.dto.orderdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.domain.enums.OrderStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrderSearchDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderStatus searchOrderStatus;
    private DeliveryStatus searchDeliveryStatus;
    private Long searchOrderId;
    private String searchMemberUserName;
    private String searchOrderItemName;


}
