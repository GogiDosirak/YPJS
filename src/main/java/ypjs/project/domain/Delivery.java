package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "delivery")
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private int deliveryId;  //배송번호

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order orderId;  //주문번호

    @Embedded
    @Column(name = "delivery_address")
    private Address deliveryAddress;  //배송주소

    @Enumerated
    @Column(name = "delivery_status")
    private CommonEnum.DeliveryStatus deliveryStatus;  //배송상태


}