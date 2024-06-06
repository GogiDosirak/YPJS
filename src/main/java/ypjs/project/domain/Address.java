package ypjs.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter  //Address 클래스는 Setter 사용해도 상관없나요?
@AllArgsConstructor
public class Address {

    @Column(name = "member_address")
    private String address;

    @Column(name = "member_address_detail")
    private String addressDetail;

    @Column(name = "member_zipcode")
    private String zipcode;
}
