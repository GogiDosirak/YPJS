package ypjs.project.dto.categorydto;

import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Category;

@Data
public class CategoryRespnseDto {

    private Long categoryId;
    private Category categoryParent;
    private String categoryName;


    public CategoryRespnseDto() {}
    public CategoryRespnseDto(Long categoryId, Category categoryParent, String categoryName) {

        this.categoryId = categoryId;
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }




}