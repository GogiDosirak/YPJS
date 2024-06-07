package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.request.ItemRequestDto;
import ypjs.project.dto.response.ItemOneDto;
import ypjs.project.dto.response.ItemResponseDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;




    @PostMapping("/ypjs/item/post")
    public ItemResponseDto saveItem(@RequestBody @Valid ItemRequestDto request) {

        Category category = categoryService.findOneCategory(request.getCategoryId());

        Item item = new Item(
                category,
                request.getItemName(),
                request.getItemContent(),
                request.getItemPrice(),
                request.getItemStock()
        );


        itemService.saveItem(item);
        return new ItemResponseDto(item.getCategory().getCategoryId(), item.getItemId(), item.getItemName(), item.getItemContent(),
                item.getItemPrice(), item.getItemStock());

    }



    @GetMapping("/ypjs/item/get/{itemId}")
    public ItemOneDto getOneItem (@PathVariable("itemId") Long itemId) {

        Item item = itemService.findOneItem(itemId);

        //조회수
        itemService.increaseItemCnt(itemId);

        return new ItemOneDto(item);

    }









}
