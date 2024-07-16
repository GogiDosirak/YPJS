package ypjs.project.dto.categorydto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Category;

@Data
public class CategoryUpdateDto {

    private Long categoryId;

    @NotNull(message = "categoryParent not be null")
    private Long categoryParent;

    @NotBlank(message = "categoryName not be null")
    private String categoryName;

    public CategoryUpdateDto() {}

    public CategoryUpdateDto(Long categoryId, Long categoryParent, String categoryName) {
        this.categoryId = categoryId;
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }
}
