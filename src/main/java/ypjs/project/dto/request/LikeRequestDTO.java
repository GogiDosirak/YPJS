package ypjs.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDTO {

    private Long memberId;
    private Long itemId;

    public LikeRequestDTO(Long memberId, Long itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }
}
