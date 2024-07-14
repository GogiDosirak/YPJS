package ypjs.project.dto.itemqnadto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.ItemQna;
import ypjs.project.domain.enums.ItemQnaStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemQnaSimpleDto {

    private Long itemQnaId;

    private Long itemId;

    private String itemName;

    private String memberName;

    private String question;

    private LocalDateTime qCreated;

    private String answer;

    private LocalDateTime aCreated;

    private ItemQnaStatus status;


    //==생성자==//
    public ItemQnaSimpleDto(ItemQna itemQna) {
        itemQnaId = itemQna.getItemQnaId();
        itemId = itemQna.getItem().getItemId();
        itemName = itemQna.getItem().getItemName();
        memberName = itemQna.getQMember().getName().substring(0,3) + "...";
        question = itemQna.getQ();
        qCreated = itemQna.getQCreated();
        answer = itemQna.getA();
        aCreated = itemQna.getACreated();
        status = itemQna.getStatus();
    }
}
