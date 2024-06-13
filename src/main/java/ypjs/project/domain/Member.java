package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import ypjs.project.domain.enums.Role;
import ypjs.project.domain.enums.Status;

import java.sql.Date;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_account_id")
    private String accountId;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_birth")
    private Date birth;

    @Column(name = "member_gender")
    private String gender;

    @Embedded
    private Address address;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_phonenumber")
    private String phoneNumber;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "member_point")
    private int point;

    @Column(name = "member_join_date")
    private Date joinDate;

    @Column(name = "member_out_date")
    private Date outDate;

    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private Status status;


}
