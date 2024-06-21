package ypjs.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ypjs.project.domain.Member;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    @GetMapping("/ypjs/board/notice/insert")
    public String insertNotice() {
        return "/board/notice/insert";
    }
}
