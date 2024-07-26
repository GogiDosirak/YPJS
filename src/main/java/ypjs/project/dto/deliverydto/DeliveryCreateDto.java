package ypjs.project.dto.deliverydto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateDto {

    @NotBlank(message = "받으실 분을 입력해주세요.")
    private String receiver;  //받으실 분

    @NotBlank(message = "휴대전화번호를 입력해주세요.")
    private String phoneNumber;  //휴대전화 번호

    @NotBlank(message = "주소를 입력해주세요.")
    private Address address;  //주소


}
