package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.service.MemberService;
import ypjs.project.service.NoticeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;
    private final MemberService memberService;

    @GetMapping("/ypjs/board/notice/notices")
    public List<NoticeDto> findAll() {
        List<Notice> noticeList = noticeService.findAll();
        List<NoticeDto> result = noticeList.stream()
                .map(n -> new NoticeDto(n.getNoticeId(), n.getNoticeTitle(), n.getNoticeContent(), n.getNoticeCnt(), n.getNoticeDate(), n.getMember().getNickname()))
                .collect(Collectors.toList());
        return result;
    }

    @PostMapping("/ypjs/board/notice/insert")
    public void insertNotice(@RequestBody @Valid NoticeRequest createNoticeRequest, HttpSession session) {
        LoginApiController.ResponseLogin responseLogin = (LoginApiController.ResponseLogin) session.getAttribute("member");
        Member member = memberService.findOne(responseLogin.getMemberId());
        noticeService.insertNotice(member, createNoticeRequest.noticeTitle,createNoticeRequest.getNoticeContent());
    }

    @PutMapping("/ypjs/board/notice/update/{noticeId}")
    public void updateNotice(@PathVariable Long noticeId,
                             @RequestBody @Valid NoticeRequest updateNoticeRequest) {
        noticeService.updateNotice(noticeId,updateNoticeRequest.getNoticeTitle(),updateNoticeRequest.getNoticeContent());
    }

    @DeleteMapping("/ypjs/board/notice/delete/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }

    @Data
    @AllArgsConstructor
    static class NoticeRequest {
        private String noticeTitle;
        private String noticeContent;
    }



    @Data
    @AllArgsConstructor
    static class NoticeDto {
        private Long noticeId;
        private String noticeTitle;
        private String noticeContent;
        private int noticeCnt;
        private LocalDateTime noticeDate;
        private String noticeWriter;
    }


}
