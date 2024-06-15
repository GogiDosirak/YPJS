package ypjs.project.dto.itemqnadto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemQnaAnswerDto {

    private Long itemQnaId;

    private Long memberId;

    private String answer;

}
