package ypjs.project.dto.deliverydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackerDto {

    private Long deliveryId;

    private String carrierId;

    private String trackId;


}
