package ypjs.project.dto.deliverydto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDto {

    private String receiver;  //받으실 분

    private String phoneNumber;  //휴대전화 번호

    private Address address;  //주소

    public DeliveryDto(String receiver, String phoneNumber, Address address) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
