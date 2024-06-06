package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.time.LocalDateTime;

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
    private String phonenumber;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @Column(name = "member_point")
    @ColumnDefault("0")
    private int point;

    @Column(name = "member_join_date")
    @CreationTimestamp
    private LocalDateTime joinDate;

    @Column(name = "member_out_date")
    private LocalDateTime outDate;

    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.MEMBER;

    // 멤버 생성 메소드
    public Member createMember(String accountId, String password, String nickname, String name, Date birth, String gender, String address, String addressDetail, String zipcode, String email, String phonenumber, LocalDateTime joinDate) {
        Member member = new Member();
        member.accountId = accountId;
        member.password = password;
        member.nickname = nickname;
        member.name = name;
        member.birth = birth;
        member.gender = gender;
        member.address = new Address(address,addressDetail,zipcode);
        member.email = email;
        member.phonenumber = phonenumber;
        member.joinDate = joinDate;
        return member;
    }

    // 멤버 수정 메소드
    public void updateMember(String accountId, String password, String nickname, String name) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;

    }


}
