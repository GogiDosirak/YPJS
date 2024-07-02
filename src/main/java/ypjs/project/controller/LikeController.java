package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.likedto.LikeRequestDto;
import ypjs.project.dto.likedto.LikeResponseDto;
import ypjs.project.dto.likedto.LikedItemDto;
import ypjs.project.service.LikeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //==좋아요와 좋아요취소 메서드==//
    @PostMapping("/ypjs/like")
    @ResponseBody
    public LikeResponseDto toggleLike(@RequestBody @Valid LikeRequestDto likeRequestDTO) {
        likeService.toggleLike(likeRequestDTO);
        return LikeResponseDto.success("좋아요를 처리했습니다!");
    }

    @GetMapping("/ypjs/like/list/{memberId}")
    public String getLikedItems(@PathVariable(name = "memberId") Long memberId,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "sortBy", defaultValue = "likeId") String sortBy,
                                @RequestParam(name = "size", defaultValue = "3") int size,
                                Model model){
        Pageable pageable = PageRequest.of(page,size);

        List<LikedItemDto> likedItems = likeService.findAllLikedItemByMemberId(memberId, pageable, sortBy);

        int allPages = likeService.countAllLikedItemByMemberId(memberId);

        int totalPages = (int) Math.ceil((double) allPages / (double) size);

        model.addAttribute("likedItems" , likedItems);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("memberId", memberId);

        return "like/list";
    }


}