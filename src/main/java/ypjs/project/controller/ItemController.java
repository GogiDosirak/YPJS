package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.categorydto.CategoryOneDto;
import ypjs.project.dto.itemdto.*;
import ypjs.project.repository.ItemReviewRepository;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemReviewService;
import ypjs.project.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemReviewService itemReviewService;
    private final CategoryService categoryService;
    private final ItemReviewRepository itemReviewRepository;


    //item등록
    @PostMapping("/ypjs/item/post")
    public ItemResponseDto saveItem(@RequestBody @Valid ItemRequestDto requestDto) {
       Item item= itemService.saveItem(requestDto);

        return new ItemResponseDto(item.getCategory().getCategoryId(), item.getItemId(), item.getItemName(), item.getItemContent(),
                item.getItemPrice(), item.getItemStock());

    }



    //item1개 조회
    @GetMapping("/ypjs/item/get/{itemId}")
    public ItemOneDto getOneItem (@PathVariable("itemId") Long itemId) {

        Item item = itemService.findOneItem(itemId);

        //조회수
        itemService.increaseItemCnt(itemId);

        //리뷰리스트
        itemReviewService.findAllItemReview(itemId);



        return new ItemOneDto(item);

    }




    //category당 아이템 조회
//    @GetMapping("/ypjs/categoryItem/get/{categoryId}")
//    public CategoryOneDto getOneCategory(@PathVariable("categoryId") Long categoryId) {
//
//        Category category =  categoryService.findOneCategory(categoryId);
//
//       List<ItemListDto> items = itemService.findAllItem(categoryId);
//
//        return new  CategoryOneDto(category, items);
//    }



    //카테고리당 아이템 조회 (페이징, 정렬)
//    @GetMapping("/ypjs/categoryItem/get/{categoryId}")
//    public CategoryOneDto getAllItem(@PathVariable("categoryId") Long categoryId,
//                                         @RequestParam(value = "offset", defaultValue = "0") int offset,
//                                         @RequestParam(value = "limit", defaultValue = "2") int limit,
//                                         @RequestParam(value = "sortBy", defaultValue = "itemId") String sortBy) {
//
//        Category category = categoryService.findOneCategory(categoryId);
//
//        List<ItemListDto> items = itemService.findAllItemSortBy(categoryId, offset, limit, sortBy);
//
//        return new CategoryOneDto(category, items);
//    }

    //카테고리당 아이템 조회(정렬,검색,페이징(페이징받아서 페이징))
    @GetMapping("/ypjs/categoryItem/get/{categoryId}")
    public CategoryOneDto getAllItem(@PathVariable("categoryId") Long categoryId,
                                     @RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "size",defaultValue = "3") int size,
                                     @RequestParam(value = "sortBy", defaultValue = "itemId") String sortBy,
                                     @RequestParam(value = "keyword", required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryService.findOneCategory(categoryId);

        List<ItemListDto> items = itemService.finaAllItemPagingSortBy(categoryId, keyword, pageable, sortBy);

        return new CategoryOneDto(category, items);

    }



    //수정
    @PutMapping("/ypjs/item/update/{itemId}")
    public void updateItem(@PathVariable("itemId") Long itemId,
                                      @RequestBody @Valid ItemUpdateDto itemUpdateDto) {

        itemService.updateItem(itemId,itemUpdateDto);
        Item findItem = itemService.findOneItem(itemId);

        //화면구현할 때 void string같은 타입으로 바꾸고 return 화면구현할 주소로 지금은 return 없어서 포스트맨 안됨 걀가값 보고 싶으면 public 옆에 ItemUpdateDto로 타입 바꾸고
        //return new ItemUpdateDto(밑에 값 써주기)
        //ItemUpdateDto response = new ItemUpdateDto(findItem.getItemId(), findItem.getCategory().getCategoryId(), findItem.getItemName(), findItem.getItemContent(), findItem.getItemPrice(),findItem.getItemStock());



    }


    //삭제
    @DeleteMapping("/ypjs/item/delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") Long itemId){
        itemService.deleteItem(itemId);
    }






}
