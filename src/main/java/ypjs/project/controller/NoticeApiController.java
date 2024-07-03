package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;
import ypjs.project.service.MemberService;
import ypjs.project.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;
    private final MemberService memberService;

//    // 공지사항 전체 조회
//    @GetMapping("/api/ypjs/board/notice/notices")
//    public Result findAll(@RequestParam int offset, @RequestParam int limit) {
//        List<Notice> noticeList = noticeService.findAll(offset,limit);
//        List<NoticeDto.NoticeApiDto> result = noticeList.stream()
//                .map(n -> new NoticeDto.NoticeApiDto(n.getNoticeId(), n.getNoticeTitle(), n.getNoticeContent(),
//                        n.getNoticeCnt(), n.getNoticeDate(), n.getMember().getNickname()))
//                .collect(Collectors.toList());
//        return new Result(result);
//    }


   // 공지사항 단건 조회
   @GetMapping("/api/ypjs/board/notice/{noticeId}")
    public NoticeDto.NoticeApiDto findOne(@PathVariable Long noticeId) {
       Notice notice = noticeService.findOne(noticeId);
        NoticeDto.NoticeApiDto result = new NoticeDto.NoticeApiDto(notice.getNoticeId(),notice.getNoticeTitle(),notice.getNoticeContent(),
               notice.getNoticeCnt(),notice.getNoticeDate(),notice.getMember().getName());
       return result;
   }

    // 글등록
    @PostMapping("/api/ypjs/board/notice/insert")
    public NoticeDto.CreateNoticeResponse insertNotice(HttpSession session, @RequestBody @Valid NoticeDto.CreateNoticeRequest createNoticeRequest) {
        Notice notice = noticeService.insertNotice(session, createNoticeRequest);
        return new NoticeDto.CreateNoticeResponse(notice.getNoticeTitle());
    }

    @PutMapping("/api/ypjs/board/notice/update/{noticeId}")
    public NoticeDto.UpdateNoticeResponse updateNotice(@PathVariable Long noticeId,
                                                       @RequestBody @Valid NoticeDto.UpdateNoticeRequest updateNoticeRequest) {
        Notice notice = noticeService.updateNotice(updateNoticeRequest);
        return new NoticeDto.UpdateNoticeResponse(notice.getNoticeTitle());
    }


    @DeleteMapping("/api/ypjs/board/notice/delete/{noticeId}")
    public NoticeDto.DeleteNoticeResponse deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return new NoticeDto.DeleteNoticeResponse(noticeId);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}
