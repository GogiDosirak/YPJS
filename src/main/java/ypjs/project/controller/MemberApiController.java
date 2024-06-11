package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.domain.Role;
import ypjs.project.domain.Status;
import ypjs.project.service.MemberService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 멤버 가입
    @PostMapping("/ypjs/member/join")
    public CreateMemberResponse join(@RequestBody @Valid CreateMemberRequest request) {
        Member member = getJoinMemberData(request);

        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);

    }

    private Member getJoinMemberData(CreateMemberRequest request) {
        Member member = new Member();
        member = member.createMember(request.getAccountId(), request.getPassword(), request.getNickname(), request.getName(), request.getBirth(), request.getGender(),
                request.getAddress(), request.getAddressDetail(), request.getZipcode(), request.getEmail(), request.getPhonenumber(), request.getJoinDate());
        return member;
    }

    // 멤버 수정
    @PutMapping("/ypjs/member/update/{memberId}")
    public UpdateMemberResponse updateMember (@PathVariable("memberId") Long memberId,
                                              @RequestBody @Valid UpdateMemberRequest request)  {
        memberService.update(memberId, request.getAccountId(), request.getPassword(), request.getNickname(), request.getName());
        Member member = memberService.findOne(memberId);
        return new UpdateMemberResponse(member.getMemberId(), member.getAccountId());
    }

    // 멤버 전체 조회
    @GetMapping("/ypjs/member/members")
    public Result members() {
        List<Member> findMembers = memberService.findAll();
        // 찾은 정보를 Dto로 변환 후, Data로 감쌈
        List<MemberDto> result = findMembers.stream()
                .map(m -> new MemberDto(m.getMemberId(),m.getAccountId(),m.getNickname()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    // 마이페이지
    @GetMapping("/ypjs/member/mypage/{memberId}")
    public Result mypage(@PathVariable("memberId") Long memberId) {
        MypageDto result = getMypageData(memberId);
        return new Result(result);

    }

    // 회원탈퇴
    @PutMapping("/ypjs/member/withdrawal/{memberId}")
    public WithdrawalMemberResponse withdraw(@PathVariable("memberId") Long memberId) {
        memberService.witrdraw(memberId);
        return new WithdrawalMemberResponse(memberId);
    }

    // member를 Dto로 감싸주는 역할
    private MypageDto getMypageData(Long memberId) {
        Member member = memberService.findOne(memberId);
        MypageDto result = new MypageDto(member.getAccountId(),member.getPassword(),member.getNickname(), member.getName(), member.getEmail(),member.getAddress(),
                member.getPhonenumber(), member.getJoinDate(), member.getRole());
        return result;
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

        @Data
    static class UpdateMemberRequest {
        private Long memberId;
        private String accountId;
        private String password;
        private String nickname;
        private String name;
        }

        @Data
        @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long memberId;
        private String accountId;
        }

        @Data
        @AllArgsConstructor
        static class Result<T> {
            private T data;
        }

        @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long memberId;
        private String accountId;
        private String nickname;
        }

        @Data
    @AllArgsConstructor
    static class MypageDto {
        private String accountId;
        private String password;
        private String nickname;
        private String name;
        private String email;
        private Address address;
        private String phonenumber;
        private LocalDateTime joinDate;
        private Role role;
        }


    @Data
    @AllArgsConstructor
    static class WithdrawalMemberResponse {
        private Long memberId;
    }

}




