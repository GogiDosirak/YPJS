package ypjs.project.dto.itemqnadto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemQnaCreateDto {

    private Long itemId;

    private Long memberId;

    private String question;

}
