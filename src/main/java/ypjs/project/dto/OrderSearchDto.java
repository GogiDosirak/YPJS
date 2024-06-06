package ypjs.project.dto;

import lombok.Getter;
import ypjs.project.domain.enums.OrderStatus;

@Getter
public class OrderSearchDto {
    private OrderStatus orderStatus;
    private String memberName;
}
