package ypjs.project.dto.noticedto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NoticeDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자가 있어야 Jackson이 JSON 데이터를 객체로 변환할 수 있음
    // 그래서 아예 @Data만 쓰거나 , All No 모두 추가해줘야함
    public static class CreateNoticeRequest {
        @NotBlank(message = "제목을 입력해주세요.")
        private String noticeTitle;
        @NotBlank(message = "내용을 입력해주세요.")
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNoticeRequest {
        private Long noticeId;
        @NotBlank(message = "제목을 입력해주세요.")
        private String noticeTitle;
        @NotBlank(message = "내용을 입력해주세요.")
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteNoticeResponse {
        private Long noticeId;
    }
    // API 스펙에 따라 달라질 수 있으므로, 지금 당장은 같더라도 두개로 분리 -> 유지보수 편해짐


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeApiDto {
        private Long noticeId;
        private String noticeTitle;
        private String noticeContent;
        private int noticeCnt;
        private LocalDate noticeDate;
        private String noticeWriter;
    }
}
