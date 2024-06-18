let memberObject = {
    init: function() {
        let _this = this;
        $("#btn-login").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.login();
        });
    },
    login: function() {
        alert("로그인이 요청되었습니다.");

        let loginForm = {
            accountId: $("#accountId").val(),
            password: $("#password").val()
        }

        console.log("Sending login request with:", loginForm); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "POST",
            url: "/ypjs/member/login",
            data: JSON.stringify(loginForm),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("Login successful:", response);
            location.href = "/"; // redirect:/가 아닌 location.href 사용
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText); // 에러 처리 개선
        });
    }
};

$(document).ready(function() {
    memberObject.init();
});