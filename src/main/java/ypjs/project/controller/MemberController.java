package ypjs.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ypjs.project.domain.Member;
import ypjs.project.dto.memberdto.MemberDto;
import ypjs.project.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입창
    @GetMapping("/ypjs/member/join")
    public String join() {
        return "member/join";
    }

//    // admin 테스트용
//    @GetMapping("/admin")
//    public String admin(Model model) {
//        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
//        return "member/admin";
//    }

    // 마이페이지
    @GetMapping("/ypjs/member/mypage/{memberId}")
    public String mypage(@PathVariable("memberId") Long memberId, Model model) {
        MemberDto.MypageDto result = getMypageData(memberId);
        model.addAttribute("mypage", result);
        return "member/mypage";
    }

    // 아이디찾기
    @GetMapping("/ypjs/member/findId")
    public String findId() {
        return "member/findId";
    }

    // 비밀번호찾기
    @GetMapping("/ypjs/member/findPassword")
        public String findPassword() {
            return "member/findPassword";
    }

    // member를 Dto로 감싸주는 역할
    private MemberDto.MypageDto getMypageData(Long memberId) {
        Member member = memberService.findOne(memberId);
        MemberDto.MypageDto result = new MemberDto.MypageDto(member.getMemberId(),member.getUsername(),member.getPassword(),member.getNickname(), member.getName(), member.getPoint(),member.getBirth(), member.getEmail(), member.getGender(), member.getAddress(),
                member.getPhonenumber(), member.getJoinDate(), member.getRole());
        return result;
    }



}
