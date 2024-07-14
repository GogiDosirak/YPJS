$(document).ready(function() {
    var IMP = window.IMP;
    IMP.init("imp53288747");


    // 포인트 사용하기 버튼 Enter 처리
    $("#usePoint").keypress(function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 동작 방지
            usePoints(); // 포인트 사용 함수 호출
        }
    });

    // 포인트 사용하기 버튼 클릭 처리
    $("#btn-usePoint").click(function() {
        usePoints(); // 포인트 사용 함수 호출
    });


    function usePoints() {
            var points = calculateUsedPoints();
            var availablePoints = parseInt(document.getElementById("availablePoints").textContent.trim());
            var orderPrice = parseInt(document.getElementById("orderPrice").textContent.trim());

            if (isNaN(points) || points <= 0) {
                alert("올바른 숫자를 입력하세요.");
                return;
            }

            if (!Number.isInteger(points)) {
                alert("포인트는 정수여야 합니다.");
                return;
            }

            if (points > availablePoints) {
                alert("사용할 수 있는 포인트를 초과했습니다.");
                return;
            }

            if (orderPrice - points < 10) {
                alert("10원 이상 결제해야 합니다.");
                return;
            }

            $("#showUsePoint").text(points + " 원");
            var finalPaymentAmount = orderPrice - points;

            if (finalPaymentAmount < 0) {
                alert("포인트 사용으로 결제 금액이 음수가 될 수 없습니다.");
                return;
            }

            updateFinalPaymentAmount(finalPaymentAmount);

            console.log("사용할 포인트:", points);
            console.log("최종 결제 금액:", finalPaymentAmount);
    }

    // 최종 결제 금액 업데이트 함수
    function updateFinalPaymentAmount(amount) {
        document.getElementById("finalPaymentAmount").textContent = amount !== undefined ? amount : orderPrice;
    }

    // 결제 취소 눌렀을 때 처리 로직
    $('.cancel-button').click(function(event) {
        event.preventDefault();
        var payId = $(this).siblings('input[type=hidden]').data('pay-id');
        $.ajax({
            url: '/api/ypjs/payment/cancel/' + payId,
            type: 'DELETE',
            success: function(response) {
                alert('주문 취소가 성공적으로 처리되었습니다.');
                window.location.reload(); // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('주문 취소 중 오류가 발생했습니다.');
                console.error(error);
                window.location.reload(); //페이지 새로고침
            }
        });
    });

    //payment.html 결제 버튼 클릭 시 처리 로직
    $("#btn-sendPayment").click(function() {
        // JavaScript 변수로부터 주문 정보 읽기
        var orderUid = $("#orderUid").text();
        var itemName = $("#itemName").text();
        var payPrice = parseInt(document.getElementById("finalPaymentAmount").textContent);
        var buyerName = $("#payName").text();
        var buyerEmail = $("#payEmail").text();
        var buyerAddress = $("#deliveryAddress").text();
        var buyerTel = $("#buyerTel").text();
        var buyerPostcode = $("#buyerPostcode").text();
        var usedPoints = calculateUsedPoints();

        // 아임포트 API를 통한 결제 요청
        IMP.request_pay({
            pg: 'html5_inicis.INIpayTest',
            pay_method: 'card',
            merchant_uid: orderUid, // 주문 번호
            name: itemName, // 상품 이름
            amount: payPrice, // 상품 가격
            buyer_email: buyerEmail, // 구매자 이메일
            buyer_name: buyerName, // 구매자 이름
            buyer_tel: buyerTel, // 구매자 번호
            buyer_addr: buyerAddress, // 구매자 주소
            buyer_postcode: buyerPostcode // 구매자 우편번호
        }, function(rsp) {
            if (rsp.success) {
                // jQuery로 HTTP 요청
                $.ajax({
                    url: "/api/ypjs/payment/payment",
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    data: JSON.stringify({
                        "payment_uid": rsp.imp_uid,      // 결제 고유번호
                        "order_uid": rsp.merchant_uid,   // 주문번호
                        "used_point": usedPoints //사용한 포인트
                    })
                }).done(function(response) {
                    console.log(response);
                    // 가맹점 서버 결제 API 성공시 로직
                    var memberId = $("#buyerMemberId").text();
                    console.log(memberId);
                    updateMemberPoints(memberId, usedPoints); // 회원 포인트 업데이트 함수 호출
                    alert('결제 완료!');
                    window.location.href = "/ypjs/payment/success-payment";
                });
            } else {
                    $.ajax({
                        url: "/api/ypjs/payment/fail-payment",
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        data: JSON.stringify({
                            "orderUid": orderUid,          // 주문 번호
                            "errorMessage": rsp.error_msg // 결제 실패 메시지
                        })
                    }).done(function(response) {
                        console.log(response);
                        alert("결제에 실패하였습니다. : " + rsp.error_msg);
                        window.location.href = "/ypjs/payment/fail-payment";
                    });
            }
        });
    });

    //사용된 포인트 계산
    function calculateUsedPoints() {
        return parseInt(document.getElementById("usePoint").value) || 0;
    }

    // 회원 포인트 업데이트 함수
    function updateMemberPoints(memberId, usedPoints) {
        $.ajax({
            url: "/api/ypjs/payment/updateMemberPoints",
            method: "POST",
            headers: { "Content-Type": "application/json" },
            data: JSON.stringify({
                memberId: memberId, // 회원 ID
                usedPoints: usedPoints // 사용된 포인트
            })
        }).done(function(response) {
        alert("회원 포인트 업데이트 성공:", response);
            console.log("회원 포인트 업데이트 성공:", response);
        }).fail(function(error) {
        alert("회원 포인트 업데이트 실패:", error);
            console.error("회원 포인트 업데이트 실패:", error);
        });
    }
});
