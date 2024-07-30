package ypjs.project.dto.itemqnadto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemQnaCreateDto {

    @NotNull
    private Long itemId;

    private Long memberId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String question;

}
