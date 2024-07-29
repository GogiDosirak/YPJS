package ypjs.project.dto.memberdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.Address;
import ypjs.project.domain.enums.Role;

import java.sql.Date;
import java.time.LocalDateTime;

public class MemberDto {
    @Data
   public static class CreateMemberRequest {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[a-z\\d]{8,16}$",
                message = "아이디 형식이 틀립니다.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$",
                message = "비밀번호 형식이 틀립니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$",
                message = "닉네임 형식이 틀립니다.")
        private String nickname;

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        private Date birth;

        @NotBlank(message = "성별을 입력해주세요.")
        private String gender;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        private String addressDetail;

        @NotBlank(message = "우편번호를 입력해주세요.")
        private String zipcode;

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "핸드폰번호를 입력해주세요.")
        private String phonenumber;

    }

    @Data
    public static class CreateMemberResponse {
        private Long memberId;

        public CreateMemberResponse(Long memberId) {
            this.memberId = memberId;
        }
    }

    @Data
    public static class UpdateMemberRequest {
        private String password;
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberResponse {
        private Long memberId;
        private String username;
    }


    @Data
    @AllArgsConstructor
    public static class MemberApiDto {
        private Long memberId;
        private String username;
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    public static class MypageDto {
        private Long memberId;
        private String username;
        private String password;
        private String nickname;
        private String name;
        private int point;
        private Date birth;
        private String gender;
        private String email;
        private Address address;
        private String phonenumber;
        private LocalDateTime joinDate;
        private Role role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class findIdRequest {
        private String name;
        private String email;
        private String phonenumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class findIdResponse {
        private String username;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class findPasswordRequest {
        private String username;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class findPasswordResponse {
        private String password;
    }


    @Data
    @AllArgsConstructor
    public static class WithdrawalMemberResponse {
        private Long memberId;
    }

}
