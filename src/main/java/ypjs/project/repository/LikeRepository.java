package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Like;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    //저장
    public void save(Like like) {
            em.persist(like);
    }

    //삭제
    public void delete(Like like) {
            em.remove(like);
    }

    //아이디에 해당 값 하나 찾기(null 구현을 위해서 Optional 사용)
    public Optional<Like> findOne(Long likeId) {
        Like like = em.find(Like.class, likeId);
        return Optional.ofNullable(like);
    }


    //회원과 상품으로 좋아요 내역 찾기
    public Optional<Like> findByMemberAndItem(Long memberId, Long itemId) {

        List<Like> likes = em.createQuery("select l from Like l where l.member.id = :memberId and l.item.id = :itemId", Like.class)
                .setParameter("memberId", memberId)
                .setParameter("itemId", itemId)
                .getResultList();
        return likes.stream().findFirst();
    }

    //페치조인으로 한번에 조회 로직
    public List<Like> findAllWithMemberItem(int offset, int limit){
        return em.createQuery(
                "select l from Like l" +
                        " join fetch l.member m" +
                        " join fetch l.item i", Like.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
