package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Member;
import ypjs.project.domain.Notice;
import ypjs.project.repository.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice findOne(Long noticeId) {
        return noticeRepository.findOne(noticeId);
    }

    public List<Notice> findAll(int offset, int limit) {
        return noticeRepository.findAll(offset,limit);
    }

    @Transactional
    public void insertNotice(Member member, String noticeTitle, String noticeContent) {
        Notice createNotice = Notice.createNotice(member, noticeTitle,noticeContent);
        noticeRepository.save(createNotice);
    }

    @Transactional
    public void updateNotice(Long noticeId, String noticeTitle, String noticeContent) {
        Notice notice = noticeRepository.findOne(noticeId);
        notice.updateNotice(noticeTitle,noticeContent);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteOne(noticeId);
    }


}
