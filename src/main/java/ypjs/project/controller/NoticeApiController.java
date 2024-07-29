package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;
import ypjs.project.service.MemberService;
import ypjs.project.service.NoticeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;


    @PostMapping("/api/ypjs/board/notice/insert")
    public NoticeDto.CreateNoticeResponse insertNotice(HttpSession session, @RequestBody @Valid NoticeDto.CreateNoticeRequest createNoticeRequest) {
        Notice notice = noticeService.insertNotice(session, createNoticeRequest);
        return new NoticeDto.CreateNoticeResponse(notice.getNoticeTitle());
    }


    @PutMapping("/api/ypjs/board/notice/update/{noticeId}")
    public NoticeDto.UpdateNoticeResponse updateNotice(@PathVariable("noticeId") Long noticeId,
                                                       @RequestBody @Valid NoticeDto.UpdateNoticeRequest updateNoticeRequest) {
        noticeService.updateNotice(updateNoticeRequest, noticeId);
        return new NoticeDto.UpdateNoticeResponse(updateNoticeRequest.getNoticeTitle());
    }


    @DeleteMapping("/api/ypjs/board/notice/delete/{noticeId}")
    public NoticeDto.DeleteNoticeResponse deleteNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return new NoticeDto.DeleteNoticeResponse(noticeId);


    }

}