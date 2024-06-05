package ypjs.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

    @Column(name = "member_address")
    private String address;

    @Column(name = "member_address_detail")
    private String addressDetail;

    @Column(name = "member_zipcode")
    private String zipcode;
}
