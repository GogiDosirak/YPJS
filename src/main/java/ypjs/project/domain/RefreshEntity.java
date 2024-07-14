package ypjs.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 토큰이 어떤 유저의 토큰인지
    private String username;

    // 유저가 들고있는 토큰 및 만료시간
    private String refresh;
    private String expiration;

}