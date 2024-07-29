package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String mailConfirm(@RequestParam("email") String email) throws Exception {
        String code = registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);
        return code;
    }

    // 아이디 중복 확인
    @PostMapping("api/ypjs/member/validateDuplication")
    public ResponseEntity<String> validateDuplication(@RequestBody String username) {
        try {
            memberService.validateButton(username);
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 아이디입니다.");
        }
    }

    // 이메일 중복 확인
    @PostMapping("/api/ypjs/member/validateEmail")
    public ResponseEntity<String> validateEmail(@RequestBody EmailRequest emailRequest) {
        System.out.println("Received email: " + emailRequest.getEmail());
        String email = emailRequest.getEmail();
        try {
            memberService.validateEmail(email);
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다");
        }
    }

    @Data
    @NoArgsConstructor
    public static class EmailRequest {
        private String email;
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

    // 아이디 찾기
    @PostMapping("/api/ypjs/member/findId")
    public ResponseEntity<MemberDto.findIdResponse> findId(@RequestBody MemberDto.findIdRequest request) {
            String username = memberService.findId(request);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MemberDto.findIdResponse("가입되지 않은 회원입니다."));
            } else {
                return ResponseEntity.ok(new MemberDto.findIdResponse(username));
            }
    }

    // 비밀번호 찾기
    @PostMapping("/api/ypjs/member/findPassword")
    public ResponseEntity<MemberDto.findPasswordResponse> findPassword(@RequestBody MemberDto.findPasswordRequest request) {
        String password = memberService.findPassword(request);
        if (password == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MemberDto.findPasswordResponse("틀린 회원정보입니다."));
        } else {
            return ResponseEntity.ok(new MemberDto.findPasswordResponse(password));
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}




