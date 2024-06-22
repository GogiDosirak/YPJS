package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
}
