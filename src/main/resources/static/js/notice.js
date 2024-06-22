let noticeObject = {
    init: function() {
        let _this = this;
        $("#btn-insert").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.insert();
        });
    },

    insert: function() {
        alert("글 등록이 요청되었습니다.");

        let request= {
            noticeTitle: $("#noticeTitle").val(),
            noticeContent: $("#noticeContent").val()
        };

        console.log("Sending insert request with:", request); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "POST",
            url: "/ypjs/board/notice/insert",
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("insert successful:", response);
            location.href = "/ypjs/board/notice/notice";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    }

};

$(document).ready(function() {
    noticeObject.init();
});