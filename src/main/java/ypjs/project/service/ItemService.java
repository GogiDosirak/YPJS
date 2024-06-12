package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.ItemRequestDto;
import ypjs.project.dto.ItemUpdateDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private  final CategoryRepository categoryRepository;



    //상품등록
    @Transactional
    public Item saveItem(ItemRequestDto itemRequestDto) {
        Category category = categoryRepository.findOneCategory(itemRequestDto.getCategoryId());
        Item item = new Item(
                category,
                itemRequestDto.getItemName(),
                itemRequestDto.getItemContent(),
                itemRequestDto.getItemPrice(),
                itemRequestDto.getItemStock());

        itemRepository.saveItem(item);

        return item;

    }



    //단건상품 조회
    public Item findOneItem(Long ItemId) {

        return itemRepository.findOne(ItemId);
    }



    //수정
    @Transactional
    public void updateItem(Long itemId, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findOne(itemId);

        Category category = categoryRepository.findOneCategory(itemUpdateDto.getCategoryId());

        item.changeItem(category,
                itemUpdateDto.getItemName(),
                itemUpdateDto.getItemContent(),
                itemUpdateDto.getItemPrice(),
                item.getItemStock());
    }


    //삭제
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
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






