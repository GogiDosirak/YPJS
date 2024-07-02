package ypjs.project.dto.categorydto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Category;

@Data
public class CategoryRequestDto {



    @NotNull(message = "categoryParent not be null")
    private Long categoryParent;

    @NotBlank(message = "categoryName not be null")
    private String categoryName;


    public CategoryRequestDto() {}
    public CategoryRequestDto(Long categoryParent, String categoryName) {


        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }



}