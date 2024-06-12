package ypjs.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private Long itemId;
    private int count;

    public OrderItemDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }
}
