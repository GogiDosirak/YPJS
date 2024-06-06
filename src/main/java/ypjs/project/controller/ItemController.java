package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.request.ItemRequestDto;
import ypjs.project.dto.response.ItemResponseDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemService;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;



    @PostMapping("/api/v2/item")
    public ItemResponseDto saveItem(@RequestBody @Valid ItemRequestDto request) {

        Category category = categoryService.findOneCategory(request.getCategoryId());

        Item item = new Item(
                category,
                request.getItemName(),
                request.getItemContent(),
                request.getPrice(),
                request.getStock()
        );


        itemService.saveItem(item);
        return new ItemResponseDto(item.getCategory().getCategoryId(), item.getItemId(), item.getItemName(), item.getItemContent(),
                item.getItemPrice(), item.getItemStock());

    }









}
