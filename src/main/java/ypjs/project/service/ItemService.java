package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.itemdto.ItemListDto;
import ypjs.project.dto.itemdto.ItemRequestDto;
import ypjs.project.dto.itemdto.ItemUpdateDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

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


    //카테고리 당 아이템 조회
//    public List<ItemListDto> findAllItem(Long categoryId) {
//        List<Item> items = itemRepository.findAllItem(categoryId);
//
//        List<ItemListDto> result = items.stream()
//                .map(ItemListDto::new)
//                .collect(Collectors.toList());
//
//        return result;
//    }



    //카테고리 당 아이템 조회 기본 최신순 정렬, 후기 많은 순, 좋아요 많은 순 추가 정렬(영한아저씨)
//    public List<ItemListDto> findAllItemSortBy(Long categoryId, int offset, int limit, String sortBy) {
//        List<Item> items = itemRepository.findAllItemSortBy(categoryId, offset, limit, sortBy);
//
//        List<ItemListDto> result = items.stream()
//                .map(ItemListDto::new)
//                .collect(Collectors.toList());
//
//        return result;
//    }


    //카테고리당 아이템 조회(정렬,검색,페이징)
    public List<ItemListDto> finaAllItemPagingSortBy(Long categoryId, String keword, Pageable pageable, String sortBy) {
        List<Item> items = itemRepository.findAllItemPagingSortByAndKeyword(categoryId, keword, pageable, sortBy);

        List<ItemListDto> result = items.stream()
                .map(ItemListDto::new)
                .collect(Collectors.toList());

        return result;

    }







}






