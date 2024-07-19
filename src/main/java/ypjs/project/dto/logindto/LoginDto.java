package ypjs.project.dto.logindto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ypjs.project.domain.enums.Role;

public class LoginDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RequestLogin {
        private String username;
        private String password;
    }

    // 로그인 응답 DTO
    @Data
    @AllArgsConstructor
    public static class ResponseLogin {
        private Long memberId;
        private String username;
        private String nickname;
        private Role role;
    }


}
