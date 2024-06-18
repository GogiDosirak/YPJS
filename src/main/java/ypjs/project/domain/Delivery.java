package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import ypjs.project.domain.enums.DeliveryStatus;

@Entity
@Table(name = "delivery")
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;  //배송번호

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;  //주문번호

    @Column(name = "delivery_receiver")
    private String receiver;  //받으실 분

    @Column(name = "delivery_phonenumber")
    private String phoneNumber;  //휴대전화 번호

    @Embedded
    @Column(name = "delivery_address")
    private Address deliveryAddress;  //배송주소

    @Enumerated
    @Column(name = "delivery_status")
    private DeliveryStatus status;  //배송상태

    //==생성자==//
    public Delivery(String receiver, String phoneNumber, Address address, DeliveryStatus deliveryStatus) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = address;
        this.status = deliveryStatus;
    }

    //==연관관계 메서드==//
    public void setOrder(Order order) {
        this.order = order;
    }


}