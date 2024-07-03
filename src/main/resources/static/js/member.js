let memberObject = {
    init: function() {
        let _this = this;
        $("#btn-login").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.login();
        });
        $("#btn-join").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.join();
        });
        $("#btn-update").on("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지
            _this.update();
        });
    },

    join: function() {
        alert("회원가입이 요청되었습니다.");

        let request = {
            accountId: $("#accountId").val(),
            password: $("#password").val(),
            nickname: $("#nickname").val(),
            name: $("#name").val(),
            birth: $("#birth").val(),
            gender: $("#gender").val(),
            address: $("#address").val(),
            addressDetail: $("#addressDetail").val(),
            zipcode: $("#zipcode").val(),
            email: $("#email").val(),
            phonenumber: $("#phonenumber").val()
        };

        console.log("Sending join request with:", request); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "POST",
            url: "/api/ypjs/member/join",
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("Join successful:", response);
            location.href = "/ypjs/member/login";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    },

    login: function() {
        alert("로그인이 요청되었습니다.");

        let loginForm = {
            accountId: $("#accountId").val(),
            password: $("#password").val()
        };

        console.log("Sending login request with:", loginForm); // 디버깅을 위한 콘솔 로그

        $.ajax({
            type: "POST",
            url: "/api/ypjs/member/login",
            data: JSON.stringify(loginForm),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            console.log("Login successful:", response);
            location.href = "/";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText); // 에러 처리 개선
        });
    },

     update: function() {
         alert("정보 수정이 요청되었습니다.");
         let id = $("#memberId").val()

         let request = {
             password: $("#password").val(),
             nickname: $("#nickname").val()
         };

         console.log("Sending update request with:", request); // 디버깅을 위한 콘솔 로그

         $.ajax({
             type: "PUT",
             url: "/api/ypjs/member/update/" + id,
             dataType: "json",
             data: JSON.stringify(request),
             contentType: "application/json; charset=utf-8"
         }).done(function(response) {
             console.log("Update successful:", response);
             location.href = "/";
         }).fail(function(jqXHR, textStatus, errorThrown) {
             console.error("Error occurred:", textStatus, errorThrown);
             alert("에러 발생: " + jqXHR.responseText); // 에러 처리 개선
         });
     }
};

$(document).ready(function() {
    memberObject.init();
});