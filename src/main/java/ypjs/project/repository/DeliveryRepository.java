package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.ItemQna;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeliveryRepository {

    @PersistenceContext
    private EntityManager em;

    public Delivery findOne(Long deliveryId) {
        return em.find(Delivery.class, deliveryId);
    }

    public List<Delivery> findAll() {
        return em.createQuery("select d from Delivery d", Delivery.class)
                .getResultList();
    }
}
