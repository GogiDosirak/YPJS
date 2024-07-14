package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Member;
import ypjs.project.dto.memberdto.MemberDto;
import ypjs.project.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 멤버 가입
    @Transactional
    public Member join(MemberDto.CreateMemberRequest request) { // 초기 메소드에만 DTO를 받아줘서 엔티티로 변환해서 반환
        Member member = CreateMemberRequestToEntity(request);
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member;
    }

    private Member CreateMemberRequestToEntity(MemberDto.CreateMemberRequest request) {
        Member member = new Member();
        member.createMember(request.getUsername(), request.getPassword(), request.getNickname(), request.getName(), request.getBirth(),
                request.getGender(), request.getAddress(), request.getAddressDetail(), request.getZipcode(), request.getEmail(), request.getPhonenumber());
        return member;
    }

    // 중복 검증
    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUsernames(member.getUsername());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("중복된 회원 아이디입니다.");
        }
    }

    // 멤버 수정
    @Transactional
    public Member update(MemberDto.UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findOne(updateMemberRequest.getMemberId());
        member.updateMember(updateMemberRequest.getPassword(), updateMemberRequest.getNickname());
        return member;
    }

    // 멤버 탈퇴
    @Transactional
    public void witrdraw(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        member.withdrawMember();
    }

    // 로그인
    public Long login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("가입되지 않은 아이디입니다."));
        if(!member.checkPassword(password)) {
            throw  new IllegalStateException("비밀번호가 틀립니다.");
        }
        return member.getMemberId();
    }

    // 출석포인트
    @Transactional
    public Member attendancePoint(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        member.attendancePoint();
        return member;
    }


    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }




    }





