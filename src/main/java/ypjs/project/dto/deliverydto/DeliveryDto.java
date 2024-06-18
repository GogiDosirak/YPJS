package ypjs.project.dto.deliverydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private String receiver;  //받으실 분

    private String phoneNumber;  //휴대전화 번호

    private Address address;  //주소


}
