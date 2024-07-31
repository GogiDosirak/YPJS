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
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.itemqnadto.ItemQnaAnswerDto;
import ypjs.project.dto.itemqnadto.ItemQnaCreateDto;
import ypjs.project.dto.itemqnadto.ItemQnaDetailDto;
import ypjs.project.dto.itemqnadto.ItemQnaSimpleDto;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.service.ItemQnaService;
import ypjs.project.service.ItemService;
import ypjs.project.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/itemqna")
public class itemQnaApiController {

    private final ItemQnaService itemQnaService;
    private final MemberService memberService;
    private final ItemService itemService;


    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ItemQnaCreateDto itemQnaCreateDto, HttpServletRequest request) {
        System.out.println("**상품문의 등록 요청됨");

        LoginDto.ResponseLogin loginMember = (LoginDto.ResponseLogin) request.getSession().getAttribute("member");

        itemQnaCreateDto.setMemberId(loginMember.getMemberId());

        Long itemId = itemQnaService.create(itemQnaCreateDto);

        return ResponseEntity.ok().body(itemId);
    }


    @PostMapping("/answer")
    public ResponseEntity answer(@RequestBody @Valid ItemQnaAnswerDto itemQnaAnswerDto) {
        System.out.println("**상품문의 답변 등록 요청됨");

        System.out.println("itemQnaId= " + itemQnaAnswerDto.getItemQnaId());
        System.out.println("aMemberId= " + itemQnaAnswerDto.getMemberId());
        System.out.println("answer= " + itemQnaAnswerDto.getAnswer());

        itemQnaService.answer(itemQnaAnswerDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{itemQnaId}")
    public ResponseDto<?> delete(@PathVariable @Valid Long itemQnaId) {
        itemQnaService.delete(itemQnaId);
        return new ResponseDto<>(HttpStatus.OK.value(), "삭제가 완료되었습니다.");
    }



}
