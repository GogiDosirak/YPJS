$(document).ready(function() {
    // Initialize member object
    memberObject.init();

    // Add input event listeners for real-time validation
    $("#username").on("input", function() {
        validateField($(this), /^[a-z\d]{8,16}$/, "형식에 맞게 작성해주세요 : 영어 8~16글자", "#error-username");
    });
    $("#password").on("input", function() {
        validateField($(this), /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/, "형식에 맞게 작성해주세요 : 소문자 + 대문자 + 숫자 + 특수문자 조합 8~16글자.", "#error-password");
    });
    $("#nickname").on("input", function() {
        validateField($(this), /^[가-힣a-zA-Z]{2,10}$/, "형식에 맞게 작성해주세요 : 한글 또는 영어 2~10글자", "#error-nickname");
    });
    $("#name").on("input", function() {
        validateField($(this), /.+/, "필수 입력 항목입니다.", "#error-name");
    });
    $("#birth").on("input", function() {
        validateField($(this), /^\d{4}-\d{2}-\d{2}$/, "형식에 맞게 작성해주세요 : yyyy-mm-dd", "#error-birth");
    });
    $("#gender").on("input", function() {
        validateField($(this), /.+/, "필수 입력 항목입니다.", "#error-gender");
    });
    $("#address").on("input", function() {
        validateField($(this), /.+/, "필수 입력 항목입니다.", "#error-address");
    });
    $("#zipcode").on("input", function() {
        validateField($(this), /^\d{5}$/, "형식에 맞게 작성해주세요 : 숫자 5자리", "#error-zipcode");
    });
    $("#email").on("input", function() {
        validateField($(this), /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/, "형식에 맞게 작성해주세요 : 이메일형식", "#error-email");
    });
    $("#phonenumber").on("input", function() {
        validateField($(this), /^\d{10,11}$/, "형식에 맞게 작성해주세요 : 숫자 10~11자리", "#error-phonenumber");
    });

    // Event listeners for buttons
    $("#btn-join").on("click", function(event) {
        event.preventDefault();
        memberObject.join();
    });

    $("#btn-login").on("click", function(event) {
        event.preventDefault();
        memberObject.login();
    });

    $("#btn-checkUsername").on("click", function(event) {
        event.preventDefault();
        memberObject.duplication();
    });

    $("#btn-update").on("click", function(event) {
        event.preventDefault();
        memberObject.update();
    });

    $("#btn-findId").on("click", function(event) {
        event.preventDefault();
        memberObject.findId();
    });

    $("#btn-findPassword").on("click", function(event) {
        event.preventDefault();
        memberObject.findPassword();
    });
});

function validateField(field, regex, errorMessage, errorElement) {
    if (!regex.test(field.val())) {
        $(errorElement).text(errorMessage);
    } else {
        $(errorElement).text("");
    }
}

let memberObject = {
    init: function() {
        // Initialization logic can go here if needed
    },

    join: function() {
        let valid = this.validate();
        if (!valid) return;

        alert("회원가입이 요청되었습니다.");

        let request = {
            username: $("#username").val(),
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

        console.log("Sending join request with:", request);

        $.ajax({
            type: "POST",
            url: "/api/ypjs/member/join",
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            alert("회원가입 성공");
            console.log("Join successful:", response);
            location.href = "/ypjs/member/login";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    },

    duplication: function() {
        let username = $("#username").val();

        $.ajax({
            type: "POST",
            url: "/api/ypjs/member/validateDuplication",
            data: username,  // 문자열로 직접 전달
            contentType: "text/plain; charset=utf-8"
        }).done(function(response) {
            alert("사용할 수 있는 아이디입니다.");
            console.log("Available username:", response);
            $("#btn-join").prop("disabled", false); // Enable join button
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("중복된 아이디입니다.");
            $("#btn-join").prop("disabled", true); // Disable join button
        });
    },

    login: function() {
        alert("로그인이 요청되었습니다.");

        let loginForm = {
            username: $("#username").val(),
            password: $("#password").val()
        };

        console.log("Sending login request with:", loginForm);

        $.ajax({
            type: "POST",
            url: "/api/ypjs/member/login",
            data: JSON.stringify(loginForm),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            alert("로그인 성공");
            console.log("Login successful:", response);
            location.href = "/";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    },

    update: function() {
        alert("정보 수정이 요청되었습니다.");
        let id = $("#memberId").val();
        console.log("Member ID:", id);

        let request = {
            password: $("#password").val(),
            nickname: $("#nickname").val()
        };

        console.log("Sending update request with:", request);

        $.ajax({
            type: "PUT",
            url: "/api/ypjs/member/update/" + id,
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            alert("정보수정 성공");
            console.log("Update successful:", response);
            location.href = "/";
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error("Error occurred:", textStatus, errorThrown);
            alert("에러 발생: " + jqXHR.responseText);
        });
    },

       findId: function() {
           let request = {
               name: $("#name").val(),
               email: $("#email").val(),
               phonenumber: $("#phonenumber").val()
           };

           $.ajax({
               type: "POST",
               url: "/api/ypjs/member/findId",
               data: JSON.stringify(request),
               contentType: "application/json; charset=utf-8"
           }).done(function(response) {
               // 응답에서 username과 message를 확인하여 처리합니다.
               if (response.username) {
                   // 아이디를 찾은 경우
                   alert("이이디는 " + response.username + " 입니다.");
                   // 또는 원하는 위치에 ID를 표시
                   // $("#result-message").text("찾은 아이디: " + response.username);
               } else if (response.message) {
                   // 에러 메시지 처리
                   alert("가입되지 않은 회원정보입니다.");
               }
               console.log("Find ID successful:", response);
               location.href = "/ypjs/member/login"; // 로그인 페이지 이동
           }).fail(function(jqXHR, textStatus, errorThrown) {
               console.error("Error occurred:", textStatus, errorThrown);
               alert("가입되지 않은 회원정보입니다.");
           });
       },

       findPassword: function() {
                  let request = {
                      username: $("#username").val(),
                      email: $("#email").val()
                  }

                  $.ajax({
                      type: "POST",
                      url: "/api/ypjs/member/findPassword",
                      data: JSON.stringify(request),
                      contentType: "application/json; charset=utf-8"
                  }).done(function(response) {
                      if (response.password) {
                          alert("비밀번호는 " + response.password + " 입니다.");
                      } else if (response.message) {
                          alert("가입되지 않은 회원정보입니다.");
                      }
                      console.log("Find Password successful:", response);
                      location.href = "/ypjs/member/login"; // 로그인 페이지 이동
                  }).fail(function(jqXHR, textStatus, errorThrown) {
                      console.error("Error occurred:", textStatus, errorThrown);
                      alert("가입되지 않은 회원정보입니다.");
                  });
              },

    validate: function() {
        let valid = true;

        // Clear previous error messages
        $(".error-message").text("");

        // Validate each field
        if (!/^[a-z\d]{8,16}$/.test($("#username").val())) {
            $("#error-username").text("형식에 맞게 작성해주세요 : 영어 8~16글자");
            valid = false;
        }
        if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/.test($("#password").val())) {
            $("#error-password").text("형식에 맞게 작성해주세요 : 소문자 + 대문자 + 숫자 + 특수문자 조합 8~16글자");
            valid = false;
        }
        if (!/^[가-힣a-zA-Z]{2,10}$/.test($("#nickname").val())) {
            $("#error-nickname").text("형식에 맞게 작성해주세요 : 한글 또는 영어 2~10글자");
            valid = false;
        }
        if (!/.+/.test($("#name").val())) {
            $("#error-name").text("필수 입력 항목입니다.");
            valid = false;
        }
        if (!/^\d{4}-\d{2}-\d{2}$/.test($("#birth").val())) {
            $("#error-birth").text("형식에 맞게 작성해주세요 : yyyy-mm-dd");
            valid = false;
        }
        if (!/.+/.test($("#gender").val())) {
            $("#error-gender").text("필수 입력 항목입니다.");
            valid = false;
        }
        if (!/.+/.test($("#address").val())) {
            $("#error-address").text("필수 입력 항목입니다.");
            valid = false;
        }
        if (!/^\d{5}$/.test($("#zipcode").val())) {
            $("#error-zipcode").text("형식에 맞게 작성해주세요 : 숫자 5자리");
            valid = false;
        }
        if (!/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test($("#email").val())) {
            $("#error-email").text("형식에 맞게 작성해주세요 : 이메일형식");
            valid = false;
        }
        if (!/^\d{10,11}$/.test($("#phonenumber").val())) {
            $("#error-phonenumber").text("형식에 맞게 작성해주세요 : 숫자 10~11자리");
            valid = false;
        }

        return valid;
    }
};