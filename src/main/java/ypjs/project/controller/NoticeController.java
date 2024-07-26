package ypjs.project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.domain.Page;
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
    public String findAll(Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size" , defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Notice> noticeList = noticeService.findAll(pageable);
        List<NoticeDto.NoticeApiDto> result = noticeList.stream()
                .map(n -> new NoticeDto.NoticeApiDto(n.getNoticeId(), n.getNoticeTitle(), n.getNoticeContent(),
                        n.getNoticeCnt(), n.getNoticeDate(), n.getMember().getNickname()))
                .collect(Collectors.toList());
        int totalPages = Page.totalPages(noticeService.countAll(), size);

        model.addAttribute("page",page);
        model.addAttribute("size",size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("noticeList", result);
        return "/board/notice/notice";
    }

    // 공지사항 단건 조회
    @GetMapping("/ypjs/board/notice/{noticeId}")
    public String findOne(@PathVariable("noticeId") Long noticeId, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Notice notice = noticeService.findOne(noticeId);
        cntUp(noticeId, request, response);
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

    public void cntUp(Long noticeId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        if(oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + noticeId.toString() + "]")) {
                noticeService.cntUp(noticeId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + noticeId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            noticeService.cntUp(noticeId);
            Cookie newCookie = new Cookie("boardView","[" + noticeId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);

        }
        // 요청이 들어오면
        // 요청에 Cookie가 없고 글을 조회한다면 [noticeId]의 값을 추가해 Cookie 생성
        // 요청에 Cookie가 있고 글을 조회한 기록이 있다면 Pass, 없다면 Cokkie에 [게시글ID] 붙이기



    }


}
