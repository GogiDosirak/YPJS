package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.RefreshEntity;

@Repository
@RequiredArgsConstructor
public class RefreshRepository {

    private final EntityManager em;

    public boolean existsByRefresh(String refresh) {
        String jpql = "SELECT COUNT(r) > 0 FROM RefreshEntity r WHERE r.refresh = :refresh";
        Boolean exists = em.createQuery(jpql, Boolean.class)
                .setParameter("refresh", refresh)
                .getSingleResult();
        return exists;
    }

    @Transactional
    public void deleteByRefresh(String refresh) {
        String jpql = "DELETE FROM RefreshEntity r WHERE r.refresh = :refresh";
        em.createQuery(jpql)
                .setParameter("refresh", refresh)
                .executeUpdate();
    }

    @Transactional
    public void save(RefreshEntity refreshEntity) {
        em.persist(refreshEntity);
    }
}