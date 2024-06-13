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

    // 공지사항 전체 조회
    @GetMapping("/ypjs/board/notice/notices")
    public List<NoticeDto> findAll(@RequestParam int offset,@RequestParam int limit) {
        List<Notice> noticeList = noticeService.findAll(offset,limit);
        List<NoticeDto> result = noticeList.stream()
                .map(n -> new NoticeDto(n.getNoticeId(), n.getNoticeTitle(), n.getNoticeContent(),
                        n.getNoticeCnt(), n.getNoticeDate(), n.getMember().getNickname()))
                .collect(Collectors.toList());
        return result;
    }

    // 공지사항 단건 조회
    @GetMapping("/ypjs/board/notice/{noticeId}")
    public NoticeDto findOne(@PathVariable Long noticeId) {
        Notice notice = noticeService.findOne(noticeId);
        NoticeDto result = new NoticeDto(notice.getNoticeId(),notice.getNoticeTitle(),notice.getNoticeContent(),
                notice.getNoticeCnt(),notice.getNoticeDate(),notice.getMember().getName());
        return result;
    }


    @PostMapping("/ypjs/board/notice/insert")
    public CreateNoticeResponse insertNotice(@RequestBody @Valid CreateNoticeRequest createNoticeRequest, HttpSession session) {
        LoginApiController.ResponseLogin responseLogin = (LoginApiController.ResponseLogin) session.getAttribute("member");
        Member member = memberService.findOne(responseLogin.getMemberId());
        noticeService.insertNotice(member, createNoticeRequest.noticeTitle,createNoticeRequest.getNoticeContent());
        return new CreateNoticeResponse(createNoticeRequest.noticeTitle);
    }

    @PutMapping("/ypjs/board/notice/update/{noticeId}")
    public UpdateNoticeResponse updateNotice(@PathVariable Long noticeId,
                             @RequestBody @Valid UpdateNoticeRequest updateNoticeRequest) {
        noticeService.updateNotice(noticeId,updateNoticeRequest.getNoticeTitle(),updateNoticeRequest.getNoticeContent());
        return new UpdateNoticeResponse(updateNoticeRequest.noticeTitle);
    }

    @DeleteMapping("/ypjs/board/notice/delete/{noticeId}")
    public DeleteNoticeResponse deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return new DeleteNoticeResponse(noticeId);
    }

    @Data
    @AllArgsConstructor
    static class CreateNoticeRequest {
        private String noticeTitle;
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    static class CreateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    static class UpdateNoticeRequest {
        private String noticeTitle;
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    static class UpdateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    static class DeleteNoticeResponse {
        private Long noticeId;
    }
    // API 스펙에 따라 달라질 수 있으므로, 지금 당장은 같더라도 두개로 분리 -> 유지보수 편해짐


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
