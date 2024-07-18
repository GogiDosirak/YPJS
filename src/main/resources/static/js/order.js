$(document).ready(function() {
//** create. html **//
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
        if (firstItemName.length > 12) {
            return firstItemName.substring(0, 12) + ' ... ';
        } else {
            return firstItemName;
        }
    }

    $('#accSummary').text(getFirstItemName());  //표시

    // accCount 업데이트: orderItemList의 개수에서 1을 뺀 값
    var itemCount = $(".list-group-item").length - 1;
    $("#accCount").text(itemCount);


    // 아이템의 총 가격 천 단위로 포맷 (+list.html)
    $('[id^=itemTotalPrice_]').each(function() {
        var price = parseInt($(this).text().replace(/,/g, ''));
        $(this).text(price.toLocaleString() + ' 원');
    });

    //총 주문 금액 계산
    var totalOrderPrice = 0;
     $('.item-count').each(function(index) {
        var itemCount = parseInt($(this).text()); // 상품 수량 가져오기
        var itemPrice = parseFloat($('#itemPrice_' + index).val()); // 상품 가격 가져오기
        var itemTotalPrice = itemCount * itemPrice; // 상품 총 가격 계산하기
        totalOrderPrice += itemTotalPrice; // 총 주문 금액에 추가
    });
    //총 주문 금액 표시
    $('#totalOrderPrice').text(totalOrderPrice.toLocaleString() + ' 원');



    //주문생성 처리
    $('#btn-create').click(function() {
        // delivery 정보
        var deliveryCreateDto = {
            receiver: $('#receiver').text(),
            phoneNumber: $('#phoneNumber').text(),
            address: {
                zipcode: $('#zipcode').text(),
                address: $('#address').text(),
                addressDetail: $('#addressDetail').text()
            }
        };

        // orderItem 리스트
        var orderItemRequestDtos = [];
        $('.list-group-item').each(function() {
            var itemId = $(this).find('input[id^="itemId_"]').val();
            var itemCount = $(this).find('span[id^="itemCount_"]').text();
            var itemPrice = $(this).find('input[id^="itemPrice_"]').val();

            var orderItemRequestDto = {
                itemId: itemId,
                itemCount: itemCount,
                itemPrice: itemPrice
            };
            orderItemRequestDtos.push(orderItemRequestDto);
        });

        // 전체 데이터
        var orderCreateDto = {
            deliveryCreateDto: deliveryCreateDto,
            orderItemRequestDtos: orderItemRequestDtos
        };

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/api/ypjs/order/create', // 실제 서버 엔드포인트 URL로 변경해야 함
            contentType: 'application/json',
            data: JSON.stringify(orderCreateDto),
            success: function(response) {
                console.log('주문이 성공적으로 전송되었습니다.');
                location = "/ypjs/payment/payment/" + response;
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 처리
                console.error('주문 전송 중 오류 발생:', error);
                // 원하는 추가 동작 구현
            }
        });
    });

//** list. html **//

    //총 주문 금액 표시
    $('[id^=price_]').each(function() {
         var price = parseInt($(this).text().replace(/,/g, ''));
         $(this).text(price.toLocaleString() + ' 원');
    });

     //종료 일자 오늘 표시
     var today = new Date().toISOString().split('T')[0];
     $('#endDate').value = today;


//** detail. html **//
    // 각 주문 목록을 순회
         $('[id^=item-list]').each(function() {
            var orderIndex = $(this).attr('id').split('_')[1];

            // 각 주문 내의 itemCount를 합산
            var totalCount = 0;
            $(this).find('[id^=itemCount_]').each(function() {
                totalCount += parseInt($(this).text());
            });

            // 합산한 값 표시
            $('#itemTotalCount_' + orderIndex).text(totalCount);
        });


//** itemGet.html **//

    // 상품 구매하기 버튼 클릭 시
        $('#btn-orderItem').click(function() {
            var cartListDtos = [];

            var itemId = $('#itemId').val();
            var itemName = $('#itemName').text();
            var itemCount = $('#product-quanity').val();
            var itemPrice = $('#itemPrice').text();

            console.log(itemId);
            console.log(itemName);
            console.log(itemCount);
            console.log(itemPrice);

            cartListDtos.push({
                itemId: itemId,
                itemName: itemName,
                itemCount: itemCount,
                itemPrice: itemPrice
            });

            if (cartListDtos.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: '/api/ypjs/order/item', // 주문하기 컨트롤러 URL
                    contentType: 'application/json;charset=UTF-8',
                    data: JSON.stringify(cartListDtos),
                    success: function(response) {
                        console.log(response);
                        location = "/ypjs/order/createFromItem?itemId=" + itemId;

                    },
                    error: function(error) {
                        console.log(error);
                        alert('에러');
                    }
                });
            } else {
                Swal.fire({
                    text: "선택된 상품이 없습니다.",
                    confirmButtonColor: '#007bff',
                    iconColor: '#007bff',
                    icon: "warning"
                });
            }
        });



});
