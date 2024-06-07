package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;



    //상품등록(등록이여서 읽기전용 아님)
    @Transactional
    public void saveItem(Item item) {

        itemRepository.saveItem(item);
    }

    //단건상품 조회
    public Item findOneItem(Long ItemId) {

        return itemRepository.findOne(ItemId);
    }

    //전체상품 조회
    public List<Item> findAllItems() {
        return itemRepository.findAllItems();
    }


    //조회수
    @Transactional
    public void increaseItemCnt(Long itemId) {
        itemRepository.increaseCnt(itemId);

    }






}
