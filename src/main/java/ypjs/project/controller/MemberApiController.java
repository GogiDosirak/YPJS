package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.dto.memberdto.MemberDto;
import ypjs.project.service.MemberService;
import ypjs.project.service.RegisterMail;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberApiController {

    private final MemberService memberService;
    private final RegisterMail registerMail;

    // 멤버 가입
    @PostMapping("/api/ypjs/member/join")
    public MemberDto.CreateMemberResponse join(@RequestBody @Valid MemberDto.CreateMemberRequest request) {
        Member member = memberService.join(request);
        return new MemberDto.CreateMemberResponse(member.getMemberId());
    }


    // 멤버 수정, Service에서 member 받아서 Dto로 변환해서 반환 / 이 방법이 맞는지 답글 달리면 확인
    @PutMapping("/api/ypjs/member/update/{memberId}")
    public MemberDto.UpdateMemberResponse updateMember (@PathVariable("memberId") Long memberId,
                                                        @RequestBody @Valid MemberDto.UpdateMemberRequest request,
                                                        HttpSession session)  {
        Member member = memberService.update(request, memberId);
        session.setAttribute("member",member); // 수정할 시 session에 정보 업데이트
        return new MemberDto.UpdateMemberResponse(member.getMemberId(), member.getUsername());
    }

    // 이메일 인증
    @PostMapping("/login/mailConfirm")
    @ResponseBody
    String mailConfirm(@RequestParam("email") String email) throws Exception {
        String code = registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);
        return code;
    }


    // 멤버 전체 조회 (관리자 페이지)
    @GetMapping("/api/ypjs/member/members")
    public Result members() {
        List<Member> findMembers = memberService.findAll();
        // 찾은 정보를 Dto로 변환 후, Data로 감쌈
        List<MemberDto.MemberApiDto> result = findMembers.stream()
                .map(m -> new MemberDto.MemberApiDto(m.getMemberId(),m.getUsername(),m.getNickname()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    // 회원탈퇴
    @PutMapping("/api/ypjs/member/withdrawal/{memberId}")
    public MemberDto.WithdrawalMemberResponse withdraw(@PathVariable("memberId") Long memberId) {
        memberService.withdraw(memberId);
        return new MemberDto.WithdrawalMemberResponse(memberId);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}




