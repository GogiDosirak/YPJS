package ypjs.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Data
public class Address {

    @Column(name = "member_address")
    private String address;
    @Column(name = "member_address_detail")
    private String addressDetail;
    @Column(name = "member_zipcode")
    private String zipcode;

    public Address() {
    }

    public Address(String address, String addressDetail, String zipcode) {
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
