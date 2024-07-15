package ypjs.project.dto.orderdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSearchDto {
    private OrderStatus orderStatus;
    private String memberName;
}
