package ypjs.project.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ypjs.project.domain.enums.OrderStatus;

@Getter
@NoArgsConstructor
public class OrderSearchDto {
    private OrderStatus orderStatus;
    private String memberName;
}
