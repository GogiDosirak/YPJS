package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.dto.request.LikeRequestDTO;
import ypjs.project.dto.response.LikeResponseDTO;
import ypjs.project.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/ypjs/like")
    public LikeResponseDTO insertLike(@RequestBody @Valid LikeRequestDTO likeRequestDTO ) throws Exception {
        likeService.insertLike(likeRequestDTO);
        return LikeResponseDTO.success("좋아요를 눌렀습니다!");
    }

    @DeleteMapping("/ypjs/like")
    public LikeResponseDTO deleteLike(@RequestBody @Valid LikeRequestDTO likeRequestDTO) throws Exception {
        likeService.deleteLike(likeRequestDTO);
        return LikeResponseDTO.success("좋아요를 취소했습니다!");
    }
}
