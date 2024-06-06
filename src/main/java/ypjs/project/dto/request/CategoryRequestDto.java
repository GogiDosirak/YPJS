package ypjs.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ypjs.project.domain.Category;

@Getter
public class CategoryRequestDto {

    @NotNull
    private Long parentId;
    private String categoryName;


    public CategoryRequestDto() {}
    public CategoryRequestDto(Long parentId, String categoryName) {

        this.parentId = parentId;
        this.categoryName = categoryName;
    }
}
