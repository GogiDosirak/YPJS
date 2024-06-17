package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.likedto.LikeRequestDto;
import ypjs.project.dto.likedto.LikeResponseDto;
import ypjs.project.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;


    //==좋아요와 좋아요취소 메서드==//
    @PostMapping("/ypjs/like")
    public LikeResponseDto toggleLike(@RequestBody @Valid LikeRequestDto likeRequestDTO) {
        likeService.toggleLike(likeRequestDTO);
        return LikeResponseDto.success("좋아요를 처리했습니다!");
    }

}