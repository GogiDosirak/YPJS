package ypjs.project.dto.categorydto;

import lombok.Getter;
import ypjs.project.domain.Category;

@Getter
public class CategoryUpdateDto {

    //private Long categoryId;
    private Long categoryParent;
    private String categoryName;

    public CategoryUpdateDto() {}

    public CategoryUpdateDto(Long categoryId, Long categoryParent, String categoryName) {
        //this.categoryId = categoryId;
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }
}
