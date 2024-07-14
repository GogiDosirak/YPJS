package ypjs.project.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;
import ypjs.project.repository.MemberRepository;
import ypjs.project.repository.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public Notice findOne(Long noticeId) {
        return noticeRepository.findOne(noticeId);
    }

//     페이징 +
//    public List<Notice> findAll(int offset, int limit) {
//        return noticeRepository.findAll(offset,limit);
//    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    @Transactional
    public Notice insertNotice(HttpSession session, NoticeDto.CreateNoticeRequest request) {
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");
        Member member = memberRepository.findOne(responseLogin.getMemberId());
        Notice notice = Notice.createNotice(member, request.getNoticeTitle(), request.getNoticeContent());
        noticeRepository.save(notice);
        return notice;
    }

    @Transactional
    public Notice updateNotice(NoticeDto.UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findOne(request.getNoticeId());
        notice.updateNotice(request.getNoticeTitle(), request.getNoticeContent());
        return notice;
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteOne(noticeId);
    }


}
