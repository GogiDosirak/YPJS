package ypjs.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Notice {

    @Id
    @GeneratedValue
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "notice_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeDate;

    @Column(name = "notice_cnt")
    private int noticeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public void cntUp(Notice notice) {
        notice.noticeCnt++;
    }


    public void updateNotice(String noticeTitle, String noticeContent) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }

    // 생성 메소드
    public static Notice createNotice(Member member, String noticeTitle, String noticeContent) {
        Notice notice = new Notice();
        notice.member = member;
        notice.noticeTitle = noticeTitle;
        notice.noticeContent = noticeContent;
        notice.noticeDate = LocalDate.now();
        return notice;
    }


}
