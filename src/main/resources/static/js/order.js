$(document).ready(function() {
    // 모달 열기
    $('#openDeliveryModal').click(function() {
        $('#deliveryModal').modal('show');
    });

    // 모달 닫기
    $('.button.btn-close').click(function() {
        $('#deliveryModal').modal('hide');
    });

    // 배송지 정보 저장
    $('#saveDeliveryInfo').click(function() {
         // 입력된 값을 배송 정보 표시 영역에 업데이트
        $('#receiver').text($('#modalReceiver').val());
        $('#phoneNumber').text($('#modalPhoneNumber').val());
        $('#address').text($('#modalAddress').val());
        $('#addressDetail').text($('#modalAddressDetail').val());
        $('#zipcode').text($('#modalZipcode').val());

        // 모달 닫기
        $('#deliveryModal').modal('hide');
    });

    //페이지 로드 시 총 결제금액 계산
     $('.total-price').each(function() {
            var count = $(this).val(); // 초기 수량
            var price = $(this).data('price'); // 상품 총액
            var index = $(this).data('index'); // 인덱스

            console.log('Index:', index, 'Count:', count, 'Price:', price); // 디버그 로그

            var totalPrice = count * price; // 초기 총 가격 계산
            $('#totalPrice').text(totalPrice.toLocaleString()); // 천 단위로 표시하여 업데이트
        });
});
