package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.dto.request.LikeRequestDTO;
import ypjs.project.dto.response.LikeResponseDTO;
import ypjs.project.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;



    //==좋아요와 좋아요취소 메서드==//
    @PostMapping("/ypjs/like")
    public LikeResponseDTO toggleLike(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.toggleLike(likeRequestDTO);
        return LikeResponseDTO.success("좋아요를 처리했습니다!");
    }

}


