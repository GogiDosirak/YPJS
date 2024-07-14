package ypjs.project.dto.memberdto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ypjs.project.domain.Member;

import java.util.ArrayList;
import java.util.Collection;

@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    // 생성자 주입 방식으로 유저엔티티 주입
    private final Member member;

    // role값을 반환
    // user에서 role값을 꺼내서 GrantedAuthority에 담은 컬렉션 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(member.getRole());
            }
        });

        return collection;
    }

    // password값을 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 아이디값을 반환
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 아래 4개는 따로 컬럼을 설정해두지 않았기 떄문에 true로 강제설정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

