package ypjs.project.dto.deliverydto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.Order;
import ypjs.project.domain.enums.DeliveryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {

    private Long deliveryId;  //배송번호

    private Long orderId;  //주문번호

    private String receiver;  //받으실 분

    private String phoneNumber;  //휴대전화 번호

    private Address address;  //배송주소

    private DeliveryStatus status;  //배송상태

    private String carrierId;  //배송사ID

    private String trackId;  //운송장번호

    public DeliveryResponseDto (Delivery d) {
        deliveryId = d.getDeliveryId();
        orderId = d.getOrder().getOrderId();
        receiver = d.getReceiver();
        phoneNumber = d.getPhoneNumber();
        address = d.getAddress();
        status = d.getStatus();
        carrierId = d.getCarrierId();
        trackId = d.getTrackId();
    }

}
