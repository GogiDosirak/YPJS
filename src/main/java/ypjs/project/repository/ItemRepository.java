package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Item findOne(Long id) { return em.find(Item.class, id);}

    //아이템 좋아요순 내림차순 정렬 조회
    public List<Item> findItemOrderByLikeCont(int offset, int limit){
        return em.createQuery(
                "select i from Item i order by i.likeCont desc",Item.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
