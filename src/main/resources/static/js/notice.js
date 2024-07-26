let noticeObject = {
    init: function() {
        let _this = this;
        $("#btn-insert").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.insert();
        });

        $("#btn-update").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.update();
        });

        $("#btn-deleteNotice").on("click", function(event) {
             event.preventDefault(); // 기본 폼 제출 방지
             _this.deleteNotice();
        });
    },

    insert: function() {
        alert("글 등록이 요청되었습니다.");

        // Get the content from Summernote
        let content = $("#noticeContent").val();

        // HTML 엔티티를 변환
        content = content
            .replace(/&nbsp;/gi, ' ')  // &nbsp;를 공백으로 변환
            .replace(/&lt;/gi, '<')   // &lt;를 <로 변환
            .replace(/&gt;/gi, '>');  // &gt;를 >로 변환

        // <br> 태그를 줄바꿈 문자로 대체
        content = content.replace(/<br\s*\/?>/gi, "\n");

        // 모든 HTML 태그 제거
        content = content.replace(/<\/?[a-z][a-z0-9]*[^>]*>/gi, '');

        let request = {
            noticeTitle: $("#noticeTitle").val(),
            noticeContent: content
        };

        console.log("Sending insert request with:", request); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "POST",
            url: "/api/ypjs/board/notice/insert",
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("Insert successful:", response);
            location.href = "/ypjs/board/notice/notices";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    },

        deleteNotice: function() {
            alert("글 삭제가 요청되었습니다.");

            // noticeId를 가져오기
            let noticeId = $("#noticeId").val();

            // URL에서 noticeId 앞에 / 추가
            $.ajax({
                type: "DELETE",
                url: "/api/ypjs/board/notice/delete/" + noticeId,
                contentType: "application/json; charset=utf-8"
            }).done(function(response) {
                console.log("Delete successful:", response);
                location.href = "/ypjs/board/notice/notices";
            }).fail(function(jqXHR, textStatus, errorThrown) {
                console.error("Error occurred:", textStatus, errorThrown);
                alert("에러 발생: " + jqXHR.responseText);
            });
        },

    update: function() {
        alert("글 수정이 요청되었습니다.");

        let noticeId = $("#noticeId").val();

        // Get the content from Summernote
        let content = $("#noticeContent").val();

        // HTML 엔티티를 변환
        content = content
            .replace(/&nbsp;/gi, ' ')  // &nbsp;를 공백으로 변환
            .replace(/&lt;/gi, '<')   // &lt;를 <로 변환
            .replace(/&gt;/gi, '>');  // &gt;를 >로 변환

        // <br> 태그를 줄바꿈 문자로 대체
        content = content.replace(/<br\s*\/?>/gi, "\n");

        // 모든 HTML 태그 제거
        content = content.replace(/<\/?[a-z][a-z0-9]*[^>]*>/gi, '');

        let request = {
            noticeTitle: $("#noticeTitle").val(),
            noticeContent: content,
            noticeId: noticeId
        };

        console.log("Sending update request with:", request); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "PUT",
            url: "/api/ypjs/board/notice/update/" + noticeId,
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("Update successful:", response);
            location.href = "/ypjs/board/notice/notices";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    }
};

$(document).ready(function() {
    noticeObject.init();
});