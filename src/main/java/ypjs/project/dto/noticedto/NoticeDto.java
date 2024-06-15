package ypjs.project.dto.noticedto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class NoticeDto {
    @Data
    @AllArgsConstructor
    public static class CreateNoticeRequest {
        @NotBlank(message = "제목을 입력해주세요.")
        private String noticeTitle;
        @NotBlank(message = "내용을 입력해주세요.")
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    public static class CreateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateNoticeRequest {
        @NotBlank(message = "제목을 입력해주세요.")
        private String noticeTitle;
        @NotBlank(message = "내용을 입력해주세요.")
        private String noticeContent;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateNoticeResponse {
        private String noticeTitle;
    }

    @Data
    @AllArgsConstructor
    public static class DeleteNoticeResponse {
        private Long noticeId;
    }
    // API 스펙에 따라 달라질 수 있으므로, 지금 당장은 같더라도 두개로 분리 -> 유지보수 편해짐


    @Data
    @AllArgsConstructor
    public static class NoticeApiDto {
        private Long noticeId;
        private String noticeTitle;
        private String noticeContent;
        private int noticeCnt;
        private LocalDateTime noticeDate;
        private String noticeWriter;
    }
}
