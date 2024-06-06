package ypjs.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeliveryDto {

    @NotNull(message = "")
    private String name;  //배송지명

    @NotNull
    private String receiver;  //받으실 분

    @NotNull
    private String phoneNumber;  //휴대전화 번호

    @NotNull
    private String address;  //주소

    @NotNull
    private String addressDetail;  //상세주소

    @NotNull
    private String zipcode;  //우편번호
}
