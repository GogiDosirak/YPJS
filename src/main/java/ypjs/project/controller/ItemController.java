package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.ItemRequestDto;
import ypjs.project.dto.ItemOneDto;
import ypjs.project.dto.ItemResponseDto;
import ypjs.project.dto.ItemUpdateDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;


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

        return new ItemOneDto(item);

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
    @DeleteMapping("ypjs/item/delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") Long itemId){
        itemService.deleteItem(itemId);
    }






}
