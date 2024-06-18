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
    $('#btn-saveDelivery').click(function() {
         // 입력된 값을 배송 정보 표시 영역에 업데이트
        $('#receiver').text($('#modalReceiver').val());
        $('#phoneNumber').text($('#modalPhoneNumber').val());
        $('#address').text($('#modalAddress').val());
        $('#addressDetail').text($('#modalAddressDetail').val());
        $('#zipcode').text($('#modalZipcode').val());

        // 모달 닫기
        $('#deliveryModal').modal('hide');
    });

    // 주문 상품 목록의 첫 번째 아이템의 이름을 가져오는 함수
    function getFirstItemName() {
        // orderItemList의 첫 번째 요소의 이름 가져오기
        var firstItemName = $("#itemName_0").text();
        // 이름이 7글자 이상인 경우 ' ... '를 붙이고, 그렇지 않으면 그대로 반환
        if (firstItemName.length > 7) {
            return firstItemName.substring(0, 7) + ' ... ';
        } else {
            return firstItemName;
        }
    }

    $('#accSummary').text(getFirstItemName());  //표시

    // accCount 업데이트: orderItemList의 개수에서 1을 뺀 값
    var itemCount = $(".list-group-item").length - 1;
    $("#accCount").text(itemCount);


    // 페이지 로드 시 각 아이템의 총 가격을 천 단위로 포맷
    $('[id^=itemTotalPriceView_]').each(function() {
        var price = parseInt($(this).text().replace(/,/g, ''));
        $(this).text(price.toLocaleString() + ' 원');
    });

    // 총 주문 금액을 초기화하는 함수
    var totalOrderPrice = 0;
    $('[id^=itemTotalPrice_]').each(function() {
        totalOrderPrice += parseInt($(this).val());
    });
    $('#totalOrderPrice').text(totalOrderPrice.toLocaleString() + ' 원');


    //주문생성 처리
    $('#btn-create').click(function() {
        // delivery 정보
        var deliveryDto = {
            receiver: $('#receiver').text(),
            phoneNumber: $('#phoneNumber').text(),
            address: {
                zipcode: $('#zipcode').text(),
                address: $('#address').text(),
                addressDetail: $('#addressDetail').text()
            }
        };

        // orderItem 리스트
        var orderItemDtos = [];
        $('.list-group-item').each(function() {
            var itemId = $(this).find('input[id^="itemId_"]').val();
            var itemCount = $(this).find('span[id^="itemCount_"]').text();
            var itemTotalPrice = $(this).find('input[id^="itemTotalPrice_"]').val();

            var orderItemDto = {
                itemId: itemId,
                itemCount: itemCount,
                itemTotalPrice: itemTotalPrice
            };
            orderItemDtos.push(orderItemDto);
        });

        // 전체 데이터
        var orderCreateDto = {
            deliveryDto: deliveryDto,
            orderItemDtos: orderItemDtos
        };

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/ypjs/order/create', // 실제 서버 엔드포인트 URL로 변경해야 함
            contentType: 'application/json',
            data: JSON.stringify(orderCreateDto),
            success: function(response) {
                console.log('주문이 성공적으로 전송되었습니다.');
                location = "/ypjs/order/detail?orderId=" + response;
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 처리
                console.error('주문 전송 중 오류 발생:', error);
                // 원하는 추가 동작 구현
            }
        });
    });
});
