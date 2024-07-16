package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Page;
import ypjs.project.dto.itemqnadto.*;
import ypjs.project.dto.ResponseDto;
import ypjs.project.service.ItemQnaService;
import ypjs.project.service.ItemService;
import ypjs.project.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/itemqna")
public class itemQnaViewController {

    private final ItemQnaService itemQnaService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/create")
    public String create(/*@RequestParam Long itemId, */Model model) {
        System.out.println("**상품문의 등록 페이지 요청됨");
        //Item i = itemService.findOneItem(itemId);

        model.addAttribute("itemId", 1/*itemId*/);
        model.addAttribute("itemName", "임시상품명"/*i.getItemName*/);
        return "itemqna/create";
    }


    @GetMapping("/list")
    public String listByItemId(
            /*@RequestParam(name = "itemId") @Valid Long itemId, */Model model,
        @RequestParam(name="page", defaultValue = "0") int page,
        @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**상품문의 내역 페이지 요청됨");
        Pageable pageable = PageRequest.of(page, size);
        Long itemId = 1L;  //임시
        List<ItemQnaSimpleDto> itemQnaList = itemQnaService.findAllByItemId(itemId, pageable);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemQnaService.countByItemId(itemId), size);

        model.addAttribute("itemQnaList", itemQnaList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        return "itemqna/list";
    }

    @GetMapping("/list/my")
    public String listByMemberId(
            /*@RequestParam(name = "memberId") @Valid Long memberId, */Model model,
        @RequestParam(name="page", defaultValue = "0") int page,
        @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**마이 상품문의 내역 페이지 요청됨");
        Pageable pageable = PageRequest.of(page, size);
        Long memberId = 1L;  //임시
        List<ItemQnaSimpleDto> itemQnaList = itemQnaService.findAllByMemberId(memberId, pageable);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemQnaService.countByMemberId(memberId), size);

        model.addAttribute("itemQnaList", itemQnaList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        return "itemqna/listMy";
    }

    @GetMapping("/list/admin")
    public String list(
            Model model,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**마이 상품문의 내역 페이지 요청됨");
        Pageable pageable = PageRequest.of(page, size);
        List<ItemQnaDetailDto> itemQnaList = itemQnaService.findAll(pageable);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemQnaService.countAll(), size);

        model.addAttribute("itemQnaList", itemQnaList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        return "itemqna/listAdmin";
    }


    @GetMapping("/detail")
    public String detailMy(@RequestParam(name = "itemQnaId") Long itemQnaId, Model model) {
        System.out.println("**상품문의 상세 페이지 요청됨");
        ItemQnaDetailDto itemQna = itemQnaService.findOne(itemQnaId);
        model.addAttribute("itemQna", itemQna);
        return "itemqna/detail";
    }


    @GetMapping("/detail/admin")
    public String detailAdmin(@RequestParam(name = "itemQnaId") Long itemQnaId, Model model) {
        System.out.println("**상품문의 상세 페이지 요청됨");
        ItemQnaDetailDto itemQna = itemQnaService.findOne(itemQnaId);
        model.addAttribute("itemQna", itemQna);
        return "itemqna/detailAdmin";
    }


    @GetMapping("/answer")
    public String answer(@RequestParam(name = "itemQnaId") Long itemQnaId, Model model, HttpServletRequest request) {
        System.out.println("**상품문의 답변 작성 페이지 요청됨");
        ItemQnaDetailDto itemQna = itemQnaService.findOne(itemQnaId);
        model.addAttribute("itemQna", itemQna);
        Member m = memberService.findOne(1L);  //임시
        model.addAttribute("aMemberAccountId",m.getUsername());
        model.addAttribute("aMemberId", m.getMemberId());
        return "itemqna/answer";
    }


}
