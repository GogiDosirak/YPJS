package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        return em.createQuery(
                "select m from Member m", Member.class)
                .getResultList();
    }


    public List<Member> findByAccountId(String accountId) {
        return em.createQuery(
                "select m from Member m where m.accountId =:accountId", Member.class)
                .setParameter("accountId", accountId)
                .getResultList();
    }

    public Member loginAccountId(String accountId) {
        return em.createQuery(
                        "select m from Member m where m.accountId =:accountId", Member.class)
                .setParameter("accountId", accountId)
                .getSingleResult();
    }




}
