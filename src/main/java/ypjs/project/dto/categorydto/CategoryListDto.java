package ypjs.project.dto.categorydto;

import lombok.Getter;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.dto.itemdto.ItemOneDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryListDto {


    private Long categoryId;
    private Long categoryParent;
    private String categoryName;

    private List<ItemDto> items;

    public CategoryListDto() {}

    public CategoryListDto(Category category) {


        categoryId = category.getCategoryId();

        // categoryParent가 null이 아닐 때만 값을 설정
        if (category.getCategoryParent() != null) {
            categoryParent = category.getCategoryParent().getCategoryId();
        } else {
            categoryParent = null;
        }

        categoryName = category.getCategoryName();

        items = category.getItems().stream()
                .map(item -> new ItemDto(item))
                .collect(Collectors.toList());

    }

    public CategoryListDto(List<CategoryListDto> categoryList, List<ItemOneDto> items) {
    }


    @Getter
    static class ItemDto{

        private String itemName;
        private String itemContent;
        private int itemPrice;
        private int itemStoock;

        public ItemDto(Item item) {
            itemName = item.getItemName();
            itemContent = item.getItemContent();
            itemPrice = item.getItemPrice();
            itemStoock = item.getItemStock();
        }
    }



}