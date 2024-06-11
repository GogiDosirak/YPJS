package ypjs.project.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ypjs.project.domain.Address;
import ypjs.project.domain.Role;
import ypjs.project.domain.Status;
import ypjs.project.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public ResponseLogin login(String accountId, String password) {
       return memberRepository.findByAccountId(accountId)
                .stream().filter(m -> m.getPassword().equals(password))
               .



    }

    // 로그인 응답 DTO
    @Data
    @AllArgsConstructor
    public static class ResponseLogin {
        private String accountId;
        private String password;
        private String nickname;
        private String gender;
        private int point;
        private String name;
        private String email;
        private Address address;
        private String phonenumber;
        private LocalDateTime joinDate;
        private Role role;
        private Status status;
    }
}
