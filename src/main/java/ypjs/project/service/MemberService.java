package ypjs.project.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.repository.MemberRepository;

import java.sql.Date;
import java.util.List;

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

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }



    }





