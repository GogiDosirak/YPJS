package ypjs.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ypjs.project.domain.Category;

@Getter
public class CategoryRequestDto {

    @NotNull
    private Long categoryParent;
    private String categoryName;


    public CategoryRequestDto() {}
    public CategoryRequestDto(Long categoryParent, String categoryName) {

        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }
}
