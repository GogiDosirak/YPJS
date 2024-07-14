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

    // 초기화 시 각 아이템의 총 가격 계산 및 표시
    $('.item-count').each(function() {
        updateItemTotalPrice($(this));
    });

    // 수량 변경 시 총 가격 계산
    $('.item-count').on('input', function() {
        updateItemTotalPrice($(this));
    });

    // 아이템의 총 가격을 업데이트하는 함수
    function updateItemTotalPrice($input) {
        var index = $input.attr('id').split('_')[1];
        var count = parseInt($input.val());
        var price = parseInt($('#itemPrice_' + index).val());
        var totalPrice = count * price;

        $('#itemTotalPrice_' + index).text(totalPrice.toLocaleString() + ' 원');
    }


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
    $('#btn-order').click(function() {
        var cartListDtos = [];

        $('.form-check-input:checked').each(function() {
            var index = $(this).attr('id').split('_')[1]; // 체크박스 ID에서 인덱스 추출
            var cartId = $('#cartId_' + index).val();
            var itemId = $('#itemId_' + index).val();
            var itemName = $('#itemName_' + index).text(); // itemName 추가
            var itemCount = $('#itemCount_' + index).val();
            var itemPrice = $('#itemPrice_' + index).val();

            cartListDtos.push({
                cartId: cartId,
                itemId: itemId,
                itemName: itemName,
                itemCount: itemCount,
                itemPrice: itemPrice
            });
        });

        if (cartListDtos.length > 0) {
            $.ajax({
                type: 'POST',
                url: '/ypjs/cart/order', // 주문하기 컨트롤러 URL
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify(cartListDtos),
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