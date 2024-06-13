package ypjs.project.dto;

import lombok.Getter;
import ypjs.project.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryOneDto {

    private Long categoryId;
    private Long categoryParent;
    private String categoryName;

    private List<ItemListDto> items;

    public CategoryOneDto() {}


    public CategoryOneDto(Category category, List<ItemListDto> items) {
        categoryId = category.getCategoryId();

        // categoryParent가 null이 아닐 때만 값을 설정
        if (category.getCategoryParent() != null) {
            categoryParent = category.getCategoryParent().getCategoryId();
        } else {
            categoryParent = null;
        }

        categoryName = category.getCategoryName();
        this.items = items;
    }

    }







