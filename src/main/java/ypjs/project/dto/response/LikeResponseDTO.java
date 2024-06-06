package ypjs.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikeResponseDTO {

    private boolean success;
    private String message;

    public static LikeResponseDTO success(String message) {
        return new LikeResponseDTO(true, message);
    }

    public LikeResponseDTO failure(String message) {
        return new LikeResponseDTO(false, message);
    }
}
