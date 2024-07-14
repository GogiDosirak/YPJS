package ypjs.project.dto.deliverydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackerDto {

    private Long deliveryId;

    private String carrierId;

    private String trackId;


}