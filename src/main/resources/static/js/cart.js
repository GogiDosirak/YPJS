$(document).ready(function() {
    // 페이지 로드 시 모든 체크박스 선택
    $('.form-check-input').prop('checked', true);

    // 전체 선택 버튼 클릭 시
    $('#selectAllCheck').click(function() {
        $('.form-check-input').prop('checked', true);
    });

    // 전체 해제 버튼 클릭 시
    $('#deselectAllCheck').click(function() {
        $('.form-check-input').prop('checked', false);
    });

    // 초기 로드 시 각 상품의 총 가격 계산
    $('.item-count').each(function() {
        var count = $(this).val(); // 초기 수량
        var price = $(this).data('price'); // 상품 단가
        var index = $(this).data('index'); // 인덱스

        console.log('Index:', index, 'Count:', count, 'Price:', price); // 디버그 로그

        var totalPrice = count * price; // 초기 총 가격 계산
        $('#itemTotalPrice_' + index).text(totalPrice.toLocaleString()); // 천 단위로 표시하여 업데이트
    });

    // 수량 입력 시 총 가격 계산
    $('.item-count').on('input', function() {
        var count = $(this).val(); // 입력된 수량
        var price = $(this).data('price'); // 상품 단가
        var index = $(this).data('index'); // 인덱스

        console.log('Index:', index, 'Count:', count, 'Price:', price); // 디버그 로그

        var totalPrice = count * price; // 총 가격 계산
        $('#itemTotalPrice_' + index).text(totalPrice.toLocaleString()); // 천 단위로 표시하여 업데이트
    });

    // 삭제 버튼 클릭 시
    $('button.custom-link').click(function(event) {
        var cartId = $(this).attr('id'); // 버튼의 ID에서 cartId 추출

        event.preventDefault(); // 기본 동작 방지

        Swal.fire({
            title: '해당 상품을 삭제 하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#007bff',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteCartItem(cartId);
            } else {
                location.reload();
            }
        });
    });

    // 상품 삭제 함수
    function deleteCartItem(cartId) {
        $.ajax({
            type: 'DELETE',
            url: '/ypjs/cart/delete?cartId=' + cartId, // URL 수정
            contentType: 'application/json;charset=UTF-8',
        }).done(function(response) {
            Swal.fire({
                text: "삭제되었습니다.",
                confirmButtonColor: '#007bff',
                iconColor: '#007bff',
                icon: "success"
            }).then((result) => {
                if (result.isConfirmed) {
                    location = "/ypjs/cart/list";
                }
            });
        }).fail(function(error) {
            console.log(error);
            alert('에러');
        });
    }

    // 주문하기 버튼 클릭 시
    $('#orderButton').click(function() {
        var orderItemDtos = [];

        $('.form-check-input:checked').each(function() {
            var index = $(this).attr('id').split('_')[1]; // 체크박스 ID에서 인덱스 추출
            var itemId = $(this).val();
            var itemName = $('#itemName_' + index).text(); // itemName 추가
            var itemCount = $('#itemCount_' + index).val();
            var itemPrice = $('#itemCount_' + index).data('price');
            var itemTotalPrice = itemCount * itemPrice;

            orderItemDtos.push({
                itemId: itemId,
                itemName: itemName,
                itemCount: itemCount,
                itemTotalPrice: itemTotalPrice
            });
        });

        if (orderItemDtos.length > 0) {
            $.ajax({
                type: 'POST',
                url: '/ypjs/cart/order', // 주문하기 컨트롤러 URL
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify(orderItemDtos),
                success: function(response) {
                    console.log(response);
                    location = "/ypjs/order/create";
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
