package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Delivery;

@Repository
@RequiredArgsConstructor
public class DeliveryRepository {

    @PersistenceContext
    private EntityManager em;

    public Delivery findOne(Long deliveryId) {
        return em.find(Delivery.class, deliveryId);
    }
}
