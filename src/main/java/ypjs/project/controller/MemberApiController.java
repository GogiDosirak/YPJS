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
import ypjs.project.dto.memberdto.MemberDto;
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
    public MemberDto.CreateMemberResponse join(@RequestBody @Valid MemberDto.CreateMemberRequest request) {
        Member member = getJoinMemberData(request);

        Long memberId = memberService.join(member);
        return new MemberDto.CreateMemberResponse(memberId);
    }


    private Member getJoinMemberData(MemberDto.CreateMemberRequest request) {
        Member member = new Member();
        member = member.createMember(request.getAccountId(), request.getPassword(), request.getNickname(), request.getName(), request.getBirth(), request.getGender(),
                request.getAddress(), request.getAddressDetail(), request.getZipcode(), request.getEmail(), request.getPhonenumber());
        return member;
    }

    // 멤버 수정
    @PutMapping("/ypjs/member/update/{memberId}")
    public MemberDto.UpdateMemberResponse updateMember (@PathVariable("memberId") Long memberId,
                                                        @RequestBody @Valid MemberDto.UpdateMemberRequest request)  {
        memberService.update(memberId, request.getAccountId(), request.getPassword(), request.getNickname(), request.getName());
        Member member = memberService.findOne(memberId);
        return new MemberDto.UpdateMemberResponse(member.getMemberId(), member.getAccountId());
    }

    // 멤버 전체 조회 (관리자 페이지)
    @GetMapping("/ypjs/member/members")
    public Result members() {
        List<Member> findMembers = memberService.findAll();
        // 찾은 정보를 Dto로 변환 후, Data로 감쌈
        List<MemberDto.MemberApiDto> result = findMembers.stream()
                .map(m -> new MemberDto.MemberApiDto(m.getMemberId(),m.getAccountId(),m.getNickname()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    // 마이페이지
    @GetMapping("/ypjs/member/mypage/{memberId}")
    public Result mypage(@PathVariable("memberId") Long memberId) {
        MemberDto.MypageDto result = getMypageData(memberId);
        return new Result(result);
    }

    // 회원탈퇴
    @PutMapping("/ypjs/member/withdrawal/{memberId}")
    public MemberDto.WithdrawalMemberResponse withdraw(@PathVariable("memberId") Long memberId) {
        memberService.witrdraw(memberId);
        return new MemberDto.WithdrawalMemberResponse(memberId);
    }

    // member를 Dto로 감싸주는 역할
    private MemberDto.MypageDto getMypageData(Long memberId) {
        Member member = memberService.findOne(memberId);
        MemberDto.MypageDto result = new MemberDto.MypageDto(member.getAccountId(),member.getPassword(),member.getNickname(), member.getName(), member.getEmail(),member.getAddress(),
                member.getPhonenumber(), member.getJoinDate(), member.getRole());
        return result;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}




