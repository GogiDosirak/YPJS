package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;






    //상품저장
    public void saveItem(Item item) {
        //새로운 객체 생성(새로운 상품 등록)
        if(item.getItemId() == null) {
            em.persist(item);
        } else {
            //item.getItemId() != null이면 수정
            em.merge(item);
        }
    }


    //상품 하나 조회
    public Item findOne(Long ItemId) {
        return em.find(Item.class, ItemId);

    }



    //상품 여러개 조회
    public List<Item> findAllItems() {
       return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


    //조회수
    @Transactional
    public void increaseCnt(Long itemId) {
        em.createQuery("update Item i set i.itemCnt = i.itemCnt + 1 where i.itemId = :itemId")
                .setParameter("itemId", itemId)
                .executeUpdate();


}







}
