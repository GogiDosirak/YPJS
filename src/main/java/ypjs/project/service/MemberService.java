package ypjs.project.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ypjs.project.common.auth.JwtToken;
import ypjs.project.common.auth.JwtTokenProvider;
import ypjs.project.domain.Address;
import ypjs.project.domain.Member;
import ypjs.project.repository.MemberRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

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
    public void update(Long memberId, String password, String nickname) {
        Member member = memberRepository.findOne(memberId);
        member.updateMember(password, nickname);
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

    @Transactional
    public JwtToken signIn(String accountId, String password) {
        // 1. accountId + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(accountId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }



    }





