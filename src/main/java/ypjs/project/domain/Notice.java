package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
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
    private LocalDateTime noticeDate;

    @Column(name = "notice_cnt")
    private int noticeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



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
        notice.noticeDate = LocalDateTime.now();
        return notice;
    }


}
