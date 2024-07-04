package ypjs.project.dto.itemdto;

import lombok.Data;
import lombok.Getter;
import ypjs.project.domain.Item;

@Data
public class ItemFileDto {


    public String itemFileName;
    public String itemFilePath;

    public ItemFileDto(){}

    public ItemFileDto(Item item) {
        itemFileName = item.getItemFilename();
        itemFilePath = item.getItemFilepath();
    }
}
