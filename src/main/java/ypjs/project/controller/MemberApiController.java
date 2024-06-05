package ypjs.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.service.MemberService;

import java.sql.Date;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/ypjs/member/join")
    public CreateMemberResponse join(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member = member.createMember(request.getAccountId(), request.getPassword(), request.getNickname(), request.getName(), request.getBirth(), request.getGender(),
                request.getAddress(), request.getAddressDetail(), request.getZipcode(), request.getEmail(), request.getPhonenumber(), request.getJoinDate());

        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);

    }



        @Data
        static class CreateMemberRequest {
            @NotEmpty
            private String accountId;
            private String password;
            private String nickname;
            private String name;
            private Date birth;
            private String gender;
            private String address;
            private String addressDetail;
            private String zipcode;
            private String email;
            private String phonenumber;
            private LocalDateTime joinDate;
        }

        @Data
        static class CreateMemberResponse {
            private Long memberId;

            public CreateMemberResponse(Long memberId) {
                this.memberId = memberId;
            }
        }
    }




