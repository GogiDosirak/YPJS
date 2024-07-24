package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;
import ypjs.project.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/ypjs/board/notice/insert")
    public String insertNotice() {
        return "/board/notice/insert";
    }


    // 공지사항 전체 조회
    @GetMapping("/ypjs/board/notice/notices")
    public String findAll(Model model) {
        List<Notice> noticeList = noticeService.findAll();
        List<NoticeDto.NoticeApiDto> result = noticeList.stream()
                .map(n -> new NoticeDto.NoticeApiDto(n.getNoticeId(), n.getNoticeTitle(), n.getNoticeContent(),
                        n.getNoticeCnt(), n.getNoticeDate(), n.getMember().getNickname()))
                .collect(Collectors.toList());
        model.addAttribute("noticeList", result);
        return "/board/notice/notice";
    }

    // 공지사항 단건 조회
    @GetMapping("/ypjs/board/notice/{noticeId}")
    public String findOne(@PathVariable("noticeId") Long noticeId, Model model, HttpSession session) {
        Notice notice = noticeService.findOne(noticeId);
        NoticeDto.NoticeApiDto result = new NoticeDto.NoticeApiDto(notice.getNoticeId(),notice.getNoticeTitle(),notice.getNoticeContent(),
                notice.getNoticeCnt(),notice.getNoticeDate(),notice.getMember().getName());
        LoginDto.ResponseLogin member = (LoginDto.ResponseLogin)session.getAttribute("member");
        if (member != null) {
            System.out.println("Member Role: " + member.getRole()); // 콘솔 로그 확인
        }
        model.addAttribute("notice",result);
        model.addAttribute("member",member);
        return "/board/notice/detail";
    }

    @GetMapping("/ypjs/board/notice/update/{noticeId}")
    public String update(@PathVariable("noticeId") Long noticeId, Model model) {
        Notice notice = noticeService.findOne(noticeId);
        NoticeDto.NoticeApiDto result = new NoticeDto.NoticeApiDto(notice.getNoticeId(),notice.getNoticeTitle(),notice.getNoticeContent(),
                notice.getNoticeCnt(),notice.getNoticeDate(),notice.getMember().getName());
        model.addAttribute("notice",result);
        return "/board/notice/update";
    }


}
