package ypjs.project.dto.likedto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDto {

    private Long memberId;
    private Long itemId;

    public LikeRequestDto(Long memberId, Long itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }
}
