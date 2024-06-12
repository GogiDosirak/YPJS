package ypjs.project.dto;

import lombok.Data;
import ypjs.project.domain.ItemQna;
import ypjs.project.domain.enums.ItemQnaStatus;

import java.time.LocalDateTime;

@Data
public class ItemQnaResponseDto {

    private Long itemQnaId;

    private String memberName;

    private String question;

    private LocalDateTime qUpdated;

    private String answer;

    private LocalDateTime aUpdated;

    private ItemQnaStatus status;


    //==생성자==//
    public ItemQnaResponseDto(ItemQna itemQna) {

        itemQnaId = itemQna.getId();
        memberName = itemQna.getQMember().getName();
        question = itemQna.getQ();

        if(itemQna.getQUpdated() == null) {
            qUpdated = itemQna.getQCreated();
        } else {
            qUpdated = itemQna.getQUpdated();
        }

        if(itemQna.getA() != null) {
            answer = itemQna.getA();
            if(itemQna.getAUpdated() == null) {
                aUpdated = itemQna.getACreated();
            } else {
                aUpdated = itemQna.getAUpdated();
            }
        }

        status = itemQna.getStatus();
    }
}
