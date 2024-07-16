package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.dto.likedto.LikeRequestDto;
import ypjs.project.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/like")

public class LikeApiController {

    private final LikeService likeService;


    //==좋아요와 좋아요취소 메서드==//
    @PostMapping("/post")
    public boolean toggleLike(@RequestBody @Valid LikeRequestDto likeRequestDTO) {
        return likeService.toggleLike(likeRequestDTO.getMemberId(), likeRequestDTO.getItemId());

    }
}