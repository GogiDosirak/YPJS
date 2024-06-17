// JavaScript 파일에서 DOMContentLoaded 이벤트 핸들러 정의
document.addEventListener("DOMContentLoaded", function() {
    var IMP = window.IMP;
    IMP.init("imp53288747");

    // 결제 버튼 클릭 시 처리 로직
    document.getElementById("payButton").addEventListener("click", function() {
        // JavaScript 변수로부터 주문 정보 읽기
        var orderUid = requestPayDto.orderId;
        var itemName = requestPayDto.itemName;
        var paymentPrice = requestPayDto.orderPrice;
        var buyerName = requestPayDto.payName;
        var buyerEmail = requestPayDto.payEmail;
        var buyerAddress = requestPayDto.deliveryAddress;
        var buyerTel = requestPayDto.phoneNumber;
        var buyerPostcode = requestPayDto.zipcode;

        // 아임포트 API를 통한 결제 요청
        IMP.request_pay({
                pg: 'html5_inicis.INIpayTest',
                pay_method: 'card',
                merchant_uid: orderUid, // 주문 번호
                name: itemName, // 상품 이름
                amount: paymentPrice, // 상품 가격
                buyer_email: buyerEmail, // 구매자 이메일
                buyer_name: buyerName, // 구매자 이름
                buyer_tel: buyerTel, // 구매자 번호
                buyer_addr: buyerAddress, // 구매자 주소
                buyer_postcode: buyerPostcode // 구매자 우편번호
            },
            function(rsp) {
                if (rsp.success) {
                    alert('call back!!: ' + JSON.stringify(rsp));
                    // 결제 성공 시: 결제 승인 또는 가상계좌 발급에 성공한 경우
                    // jQuery로 HTTP 요청
                    jQuery.ajax({
                        url: "/ypjs/payment",
                        method: "POST",
                        headers: {"Content-Type": "application/json"},
                        data: JSON.stringify({
                            "payment_uid": rsp.imp_uid,      // 결제 고유번호
                            "order_uid": rsp.merchant_uid   // 주문번호
                        })
                    }).done(function (response) {
                        console.log(response);
                        // 가맹점 서버 결제 API 성공시 로직
                        alert('결제 완료!');
                        window.location.href = "/ypjs/payment/success-payment";
                    });
                } else {
                    alert("success? "+ rsp.success+ ", 결제에 실패하였습니다. 에러 내용: " + JSON.stringify(rsp));
                    alert('결제 실패!');
                    window.location.href = "/ypjs/payment/fail-payment";
                }
            });
    });
});
