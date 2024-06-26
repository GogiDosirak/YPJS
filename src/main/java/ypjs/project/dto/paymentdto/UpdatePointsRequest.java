package ypjs.project.dto.paymentdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePointsRequest {
        private Long memberId;
        private int usedPoints;
}
