package ypjs.project.dto.itemqnadto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ypjs.project.domain.ItemQna;
import ypjs.project.domain.enums.ItemQnaStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemQnaDetailDto {

    private Long itemQnaId;

    private Long itemId;

    private String itemName;

    private String qMemberUserName;

    private String question;

    private LocalDateTime qCreated;

    private LocalDateTime qUpdated;

    private String aMemberUserName;

    private String answer;

    private LocalDateTime aCreated;

    private LocalDateTime aUpdated;

    private ItemQnaStatus status;


    //==생성자==//
    public ItemQnaDetailDto(ItemQna itemQna) {
        itemQnaId = itemQna.getItemQnaId();
        itemId = itemQna.getItem().getItemId();
        itemName = itemQna.getItem().getItemName();
        qMemberUserName = itemQna.getQMember().getUsername();
        question = itemQna.getQ();
        qCreated = itemQna.getQCreated();
        qUpdated = itemQna.getQUpdated();
        if(itemQna.getAMember() != null) {
            aMemberUserName = itemQna.getAMember().getUsername();
        }
        answer = itemQna.getA();
        aCreated = itemQna.getACreated();
        aUpdated = itemQna.getAUpdated();
        status = itemQna.getStatus();
    }
}
