package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Member;
import ypjs.project.dto.paymentdto.UpdatePointsRequest;
import ypjs.project.repository.MemberRepository;

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

    //payment 포인트 업데이트 관련 로직
    @Transactional
    public boolean updateMemberPoints(UpdatePointsRequest request) {

            Long memberId = request.getMemberId();
            int usedPoints = request.getUsedPoints();

            Member member = memberRepository.findOne(memberId);
            if (member == null) {
                throw new IllegalArgumentException("회원이 존재하지 않습니다.");
            }
            int currentPoints = member.getPoint();
            int newPoints = currentPoints - usedPoints;

            if (newPoints < 0) {
                throw new IllegalArgumentException("사용할 포인트가 현재 포인트보다 많습니다.");
            }

            member.updatePoint(newPoints);
            memberRepository.save(member);

            return true;

        }
    }







