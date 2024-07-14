package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ypjs.project.domain.Member;
import ypjs.project.dto.memberdto.CustomUserDetails;
import ypjs.project.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // DB에 접근해야하므로 Repository 의존성 주입
    private final MemberRepository memberRepository;


    // DB에서 특정 유저를 조회해서 return
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        // 조회한 데이터를 검증
        if (memberData != null) {
            // 아이디를 가진 회원이 있다면, CustomUserDetails에 넣어서 반환 -> AuthenticationManager에서 비밀번호도 맞는지 검증
            return new CustomUserDetails(memberData);
        }

        return null;
    }
}