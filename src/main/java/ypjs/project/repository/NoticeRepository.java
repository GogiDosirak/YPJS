package ypjs.project.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ypjs.project.domain.Notice;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {
    private final EntityManager em;

    // 공지사항 단건 조회
    public Notice findOne(Long noticeId) {
        return em.find(Notice.class, noticeId);
    }

//    // 공지사항 전체 조회 페이징+
//    public List<Notice> findAll(int offset, int limit) {
//        return em.createQuery(
//                "select n from Notice n", Notice.class)
//                .setFirstResult(offset)
//                .setMaxResults(limit)
//                .getResultList();
//    }

    public List<Notice> findAll(Pageable pageable) {
        return em.createQuery(
                        "select n from Notice n order by n.noticeId desc", Notice.class)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    public int countAll() {
        return em.createQuery("select count(n) from Notice n", Long.class)
                .getSingleResult()
                .intValue();
    }

    // 공지사항 등록
    public void save(Notice notice) {
        em.persist(notice);
    }

    // 공지사항 삭제
    public void deleteOne(Long noticeId) {
        em.createQuery(
                "delete from Notice n where n.noticeId =:noticeId")
                .setParameter("noticeId",noticeId)
                .executeUpdate(); // DB에 영향을 주는 DML 작업 실행
    }

    // 공지사항 검색(제목)
    public List<Notice> findByTitle(String searchNoticeTitle) {
        return em.createQuery(
                "select n from notice n where n.noticeTitle like concat('%', :searchNoticeTitle, '%")
                .setParameter("searchNoticeTitle", searchNoticeTitle)
                .getResultList();
    }


    // 공지사항 검색(내용)
    public List<Notice> findByContent(String searchNoticeContent) {
        return em.createQuery(
                "select n from Notice n where n.noticeContent like concat('%', :searchNoticeContent, '%'")
                .setParameter("searchNoticeContent",searchNoticeContent)
                .getResultList();
    }






}
