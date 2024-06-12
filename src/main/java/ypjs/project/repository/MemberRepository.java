package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Member;

@Repository
public class MemberRepository {

    @PersistenceContext  //엔티티 메니저 주입
    private EntityManager em;

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }
}
