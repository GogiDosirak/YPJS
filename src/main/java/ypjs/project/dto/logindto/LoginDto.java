package ypjs.project.dto.logindto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ypjs.project.domain.Address;
import ypjs.project.domain.enums.Role;
import ypjs.project.domain.enums.Status;


import java.time.LocalDateTime;

public class LoginDto {
    // 로그인 응답 DTO
    @Data
    @AllArgsConstructor
    public static class ResponseLogin {
        private Long memberId;
        private String accountId;
        private String nickname;
    }


}
