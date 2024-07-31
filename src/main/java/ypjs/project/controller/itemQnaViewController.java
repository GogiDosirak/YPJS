package ypjs.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Item;
import ypjs.project.domain.Member;
import ypjs.project.domain.Page;
import ypjs.project.domain.enums.Role;
import ypjs.project.dto.itemqnadto.*;
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.logindto.LoginDto;
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
    public String create(
            @RequestParam(name = "itemId") @Valid Long itemId, Model model, HttpServletRequest request) {
        System.out.println("**상품문의 등록 페이지 요청됨");

        HttpSession session = request.getSession();
        //멤버정보
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");

        if(loginMember == null) {
            request.setAttribute("msg", "로그인이 필요합니다.");
            request.setAttribute("url", "/ypjs/member/login");
            return "alert";
        }

        Item i = itemService.findOneItem(itemId);

        model.addAttribute("itemId", itemId);
        model.addAttribute("itemName", i.getItemName());
        return "itemqna/create";
    }


    @GetMapping("/list/{itemId}")
    public String listByItemId(
            @PathVariable(name = "itemId") @Valid Long itemId,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size,
            Model model) {
        System.out.println("**상품문의 내역 페이지 요청됨");
        Pageable pageable = PageRequest.of(page, size);
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
            HttpServletRequest request, Model model,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**마이 상품문의 내역 페이지 요청됨");

        HttpSession session = request.getSession();
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");

        if(loginMember == null) {
            request.setAttribute("msg", "로그인이 필요합니다.");
            request.setAttribute("url", "/ypjs/member/login");
            return "alert";
        }

        Pageable pageable = PageRequest.of(page, size);
        List<ItemQnaSimpleDto> itemQnaList = itemQnaService.findAllByMemberId(loginMember.getMemberId(), pageable);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemQnaService.countByMemberId(loginMember.getMemberId()), size);

        model.addAttribute("itemQnaList", itemQnaList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        return "itemqna/listMy";
    }

    @GetMapping("/list/admin")
    public String list(
            HttpServletRequest request, Model model,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**관리자 상품문의 내역 페이지 요청됨");

        HttpSession session = request.getSession();
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");
        
        Member member = memberService.findOne(loginMember.getMemberId());

        /*로그인 멤버가 관리자가 아닌 경우*/
        if(member.getRole() != Role.ROLE_ADMIN) {
            request.setAttribute("msg", "잘못된 접근입니다.");
            request.setAttribute("url", "/index");
            return "alert";
        }

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

        HttpSession session = request.getSession();
        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) session.getAttribute("member");

        Member m = memberService.findOne(loginMember.getMemberId());

        model.addAttribute("aMemberUserName",m.getUsername());
        model.addAttribute("aMemberId", m.getMemberId());

        return "itemqna/answer";

    }


}
