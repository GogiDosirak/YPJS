package ypjs.project.controller;


import jakarta.servlet.http.HttpSession;

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

import ypjs.project.dto.logindto.LoginDto;

import ypjs.project.service.LikeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/like")
public class LikeController {

    private final LikeService likeService;

    //like 버튼 테스트
    @GetMapping("/likeTest")
    public String likeButtonTest(Model model){
        //todo : 하드코딩된 값 바꿔치기 해야함
        Long itemId = 1L;
        model.addAttribute("itemId", itemId);
        return "like/likeTest";
    }


    //좋아요한 목록 보기
    @GetMapping("/list/{memberId}")
    public String getLikedItems(@PathVariable(name = "memberId") Long memberId,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "sortBy", defaultValue = "likeId") String sortBy,
                                @RequestParam(name = "size", defaultValue = "3") int size,
                                Model model, HttpSession session){

        //로그인한 값이랑 같은 memberId 인지 확인하는 로직
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");
        if (responseLogin == null || !responseLogin.getMemberId().equals(memberId)) {
            return "redirect:/";
        }


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