package ypjs.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.dto.ItemQnaAnswerDto;
import ypjs.project.dto.ItemQnaCreateDto;
import ypjs.project.dto.ItemQnaUpdateDto;
import ypjs.project.dto.ResponseDto;
import ypjs.project.service.ItemQnaService;
import ypjs.project.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypjs/itemQna")
public class itemQnaController {

    private final ItemQnaService itemQnaService;
    private final MemberService memberService;

    @GetMapping("/list/{itemId}")
    public String listByItemId(@PathVariable @Valid Long itemId, Model model) {
        model.addAttribute("itemQnaDtos", itemQnaService.findAllByItemId(itemId));
        return "html";
    }

    @GetMapping("/list/{memberId}")
    public  String listByMemberId(@PathVariable @Valid Long memberId, Model model) {
        model.addAttribute("itemQnaDtos", itemQnaService.findAllByMemberId(memberId));
        return "html";
    }

    @GetMapping("/create")
    public String create() {
        return "itemQnaCreate";
    }

    @PostMapping("/create")
    public String create(@RequestBody @Valid ItemQnaCreateDto itemQnaCreateDto) {
        itemQnaService.create(itemQnaCreateDto);
        return "redirect:#";
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
