package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import ypjs.project.domain.enums.Role;
import ypjs.project.domain.enums.Status;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Member implements UserDetails {

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
    private Role role;

    @Column(name = "member_point")
    private int point;

    @Column(name = "member_join_date")
    @CreationTimestamp
    private LocalDateTime joinDate;

    @Column(name = "member_out_date")
    private LocalDateTime outDate;

    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "member_point_date")
    private LocalDateTime pointDate;

    // 멤버 생성 메소드
    public Member createMember(String accountId, String password, String nickname, String name, Date birth, String gender, String address, String addressDetail, String zipcode, String email, String phonenumber) {
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
        member.joinDate = LocalDateTime.now();
        member.status = Status.MEMBER;
        member.role = Role.CUSTOMER;
        member.roles.add(String.valueOf(Status.MEMBER));
        member.roles.add(String.valueOf(Role.CUSTOMER));
        return member;
    }

    // 멤버 수정 메소드
    public void updateMember(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;

    }

    // 멤버 탈퇴 메소드
    public void withdrawMember() {
        this.outDate = LocalDateTime.now();
        this.status = Status.WITHDRAWAL;
    }

    // 비밀번호 확인 메소드
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }



    // 출석 포인트 적립 메소드
    public void attendancePoint() {
        if(pointDate == null) {
            this.pointDate = LocalDateTime.now();
            this.point += 500; // 첫출석 포인트
        } else if(pointDate.getDayOfMonth() != LocalDateTime.now().getDayOfMonth()) {
            this.pointDate = LocalDateTime.now();
            this.point += 50;
        }
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.accountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }






}
