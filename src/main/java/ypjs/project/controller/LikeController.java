package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.dto.likedto.LikedItemDto;
import ypjs.project.service.LikeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/like")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/list/{memberId}")
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