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
import ypjs.project.dto.itemqnadto.*;
import ypjs.project.dto.ResponseDto;
import ypjs.project.service.ItemQnaService;
import ypjs.project.service.ItemService;
import ypjs.project.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/itemqna")
public class itemQnaController {

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

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid ItemQnaCreateDto itemQnaCreateDto, HttpServletRequest request) {
        System.out.println("**상품문의 등록 요청됨");
        //HttpSession session = request.getSession();
        //Long memberId = (Long) session.getAttribute("memberId");
        //itemQnaCreateDto.setMemberId(memberId);

        itemQnaCreateDto.setMemberId(1L);
        Long itemId = itemQnaService.create(itemQnaCreateDto);
        return ResponseEntity.ok().body(itemId);
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
        model.addAttribute("itemQnaList", itemQnaList);
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
        model.addAttribute("itemQnaList", itemQnaList);
        return "itemqna/listMy";
    }

    @GetMapping("/detail")
    public String detailMy(@RequestParam(name = "itemQnaId") Long itemQnaId, Model model) {
        System.out.println("**상품문의 상세 페이지 요청됨");
        ItemQnaDetailDto itemQna = itemQnaService.findOne(itemQnaId);
        model.addAttribute("itemQna", itemQna);
        return "itemqna/detail";
    }

    @GetMapping("/list/admin")
    public String list(
        Model model,
        @RequestParam(name="page", defaultValue = "0") int page,
        @RequestParam(name="size", defaultValue = "10") int size) {
        System.out.println("**마이 상품문의 내역 페이지 요청됨");
        Pageable pageable = PageRequest.of(page, size);
        List<ItemQnaDetailDto> itemQnaList = itemQnaService.findAll(pageable);
        model.addAttribute("itemQnaList", itemQnaList);
        return "itemqna/listAdmin";
    }

    @GetMapping("/detail/admin")
    public String detailAdmin(@RequestParam(name = "itemQnaId") Long itemQnaId, Model model) {
        System.out.println("**상품문의 상세 페이지 요청됨");
        ItemQnaDetailDto itemQna = itemQnaService.findOne(itemQnaId);
        model.addAttribute("itemQna", itemQna);
        return "itemqna/detailAdmin";
    }


    @GetMapping("/answer")
    public String answer() {
        return "itemQnaAnswer";
    }

    @PostMapping("/answer")
    public String answer(@RequestBody @Valid ItemQnaAnswerDto itemQnaAnswerDto) {
        itemQnaService.answer(itemQnaAnswerDto);
        return "redirect:#";
    }

    @GetMapping("/updateQ")
    public String updateQ() {
        return "itemQnaUpdateQ";
    }

    @GetMapping("/updateA")
    public String updateA() {
        return "itemQnaUpdateA";
    }

    @PostMapping("/update")
    public String update(@RequestBody @Valid ItemQnaUpdateDto itemQnaUpdateDto) {
        itemQnaService.update(itemQnaUpdateDto);
        return "redirect:#";
    }

    @DeleteMapping("/delete/{itemQnaId}")
    public ResponseDto<?> delete(@PathVariable @Valid Long itemQnaId) {
        itemQnaService.delete(itemQnaId);
        return new ResponseDto<>(HttpStatus.OK.value(), "삭제가 완료되었습니다.");
    }



}
