package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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


    public Optional<Member> findByUsername(String username) {
        List<Member> members = em.createQuery(
                        "select m from Member m where m.username = :username", Member.class)
                .setParameter("username",username)
                .getResultList();
        if (members.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(members.get(0));
        }
    }

    public List<Member> findByUsernames(String username) {
        return  em.createQuery(
                        "select m from Member m where m.username = :username", Member.class)
                .setParameter("username",username)
                .getResultList();
    }

    public List<Member> findByEmail(String email) {
        return  em.createQuery(
                        "select m from Member m where m.email = :email", Member.class)
                .setParameter("email",email)
                .getResultList();
    }

    public Member findId(String name, String email, String phonenumber) {
       try {
           return em.createQuery(
                           "select m from Member m where m.email = :email and m.name = :name and m.phonenumber = :phonenumber", Member.class)
                   .setParameter("email", email)
                   .setParameter("name", name)
                   .setParameter("phonenumber", phonenumber)
                   .getSingleResult();
       } catch (NoResultException e) {
           return null;
       }

    }

    public Member findPassword(String username, String email) {
        try {
            return em.createQuery(
                    "select m from Member m where m.username = :username and m.email = :email",Member.class)
                    .setParameter("username",username)
                    .setParameter("email",email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }






}
