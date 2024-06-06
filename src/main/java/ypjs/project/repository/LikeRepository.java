package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Like;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    public void save(Like like) {
        if (like.getLikeId() == null) {
            em.persist(like);
        } else {
            em.merge(like);
        }
    }

    public void delete(Like like) {
    }

    public Like findByMemberAndItem(Long memberId, Long itemId) {

        return em.createQuery("select l from Like l " +
                        "where l.member.id = :memberId " +
                        "and l.item.id = :itemId", Like.class)
                .setParameter("memberId", memberId)
                .setParameter("itemId", itemId)
                .getSingleResult();
    }
}
