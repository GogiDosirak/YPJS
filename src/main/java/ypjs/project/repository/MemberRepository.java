package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private  final EntityManager em;


    //멤버한명 조회
    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }
}
