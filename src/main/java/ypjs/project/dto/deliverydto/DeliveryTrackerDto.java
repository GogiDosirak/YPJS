package ypjs.project.dto.deliverydto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackerDto {

    @NotNull(message = "배송 정보가 없습니다.")
    private Long deliveryId;

    @NotNull(message = "운송회사 정보가 없습니다.")
    private String carrierId;

    @NotNull(message = "운송장번호 정보가 없습니다.")
    private String trackId;


}
