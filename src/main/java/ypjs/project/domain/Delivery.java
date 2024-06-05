package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import ypjs.project.domain.enums.DeliveryStatus;

@Entity
@Table(name = "delivery")
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long deliveryId;  //배송번호

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;  //주문번호

    @Column(name = "delivery_name")
    private String deliveryName;  //배송지이름

    @Embedded
    private Address deliveryAddress;  //배송주소

    @Enumerated
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;  //배송상태

    //==연관관계 메서드==//
    public void setOrder(Order order) {
        this.order = order;
    }

}