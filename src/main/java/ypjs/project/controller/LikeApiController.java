package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Item;
import ypjs.project.dto.likedto.LikeRequestDto;
import ypjs.project.dto.likedto.LikeResponseDto;
import ypjs.project.service.LikeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    //==좋아요순 아이템 조회==//
    @GetMapping("/ypjs/board/like/likedItems")
    public List<ItemDto> findItemOrderByLikeCont(@RequestParam int offset, @RequestParam int limit){
        List<Item> itemList = likeService.findItemOrderByLikeCont(offset, limit);
        List<ItemDto> result = itemList.stream()
                .map(i->new ItemDto(i.getItemId(), i.getCategory().toString(),i.getItemName(),i.getItemPrice(),i.getItemFilename(),i.getItemFilepath(),i.getLikeCont()))
                .collect(Collectors.toList());
        return result;
    }


    //==좋아요와 좋아요취소 메서드==//
    @PostMapping("/ypjs/like")
    public LikeResponseDto toggleLike(@RequestBody @Valid LikeRequestDto likeRequestDTO) {
        likeService.toggleLike(likeRequestDTO);
        return LikeResponseDto.success("좋아요를 처리했습니다!");
    }

    @Data
    @AllArgsConstructor
    static class ItemDto{
        private Long itemId;
        private String category;
        private String itemName;
        private int itemPrice;
        private String itemFilename;
        private String itemFilepath;
        private int likeCont;
    }
}


