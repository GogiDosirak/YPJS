package ypjs.project.dto.likedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikeResponseDto {

    private boolean success;
    private String message;

    public static LikeResponseDto success(String message) {
        return new LikeResponseDto(true, message);
    }

    public LikeResponseDto failure(String message) {
        return new LikeResponseDto(false, message);
    }
}
