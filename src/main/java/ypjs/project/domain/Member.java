package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Member  {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;


    @Column(name = "member_username", unique = true)
    private String username;

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

    @Column(name = "member_email", unique = true)
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
    public void createMember(String username, String password, String nickname, String name, Date birth, String gender, String address, String addressDetail, String zipcode, String email, String phonenumber) {
//       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.username = username;
//        this.password = bCryptPasswordEncoder.encode(password); //(JWT 버전 쓸때 다시 살리기)
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.address = new Address(address,addressDetail,zipcode);
        this.email = email;
        this.phonenumber = phonenumber;
        this.joinDate = LocalDateTime.now();
        this.status = Status.MEMBER;
        this.role = Role.ROLE_ADMIN;
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

    // 포인트 세팅 메소드
    public void updatePoint(int newPoints) {
        if (newPoints < 0) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다.");
        }
        this.point = newPoints;
    }








}
