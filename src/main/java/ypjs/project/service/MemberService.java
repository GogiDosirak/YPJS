package ypjs.project.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.domain.Role;
import ypjs.project.domain.Status;
import ypjs.project.repository.MemberRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getMemberId();
    }

    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByAccountId(member.getAccountId());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("중복된 회원 아이디입니다.");
        }
    }

    @Transactional
    public void update(Long memberId, String accountId, String password, String nickname, String name) {
        Member member = memberRepository.findOne(memberId);
        member.updateMember(accountId, password, nickname, name);
    }

    @Transactional
    public void witrdraw(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        member.withdrawMember();
    }

    // 로그인
    public Long login(String accountId, String password) {
        Member member = memberRepository.loginAccountId(accountId)
                .orElseThrow(() -> new IllegalStateException("가입되지 않은 아이디입니다."));
        if(!member.checkPassword(password)) {
            throw  new IllegalStateException("비밀번호가 틀립니다.");
        }
        return member.getMemberId();
    }


    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }



    }





