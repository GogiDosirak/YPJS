package ypjs.project.dto.orderdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrderSearchDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderStatus searchOrderStatus;
    private Long searchOrderId;
    private String searchMemberAccountId;
    private String searchOrderItemName;


}
