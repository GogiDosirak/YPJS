$(document).ready(function() {

//상품문의 생성 처리
    $('#btn-create').click(function() {
        var itemId = $('#itemId').val();
        var question = $('#question').val();

        console.log(itemId);
        console.log(question);

        var itemQnaCreateDto = {
            itemId: itemId,
            question: question
        }

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/api/ypjs/itemqna/create',
            contentType: 'application/json',
            data: JSON.stringify(itemQnaCreateDto),
            success: function(response) {
                console.log('상품문의 등록 완료');
                Swal.fire({
                    text: "상품문의가 등록되었습니다.",
                    confirmButtonColor: '#007bff',
                    iconColor: '#007bff',
                    icon: "success"
                }).then((result) => {
                    if (result.isConfirmed) {
                        //상품 상세로 이동
                        //location = "/ypjs/item/detail?itemId=" + response;
                        //임시 로케이션
                        location = "/ypjs/order/hello";
                    }
                });
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 처리
                console.error('오류 발생:', error);
            }
        });
    });

/** myList.html **/

//목록에서 행 클릭하면 상세페이지로 이동
    $('tbody').on('click', 'tr', function() {
        var itemQnaId = $(this).attr('id');
        location = '/ypjs/itemqna/detail?itemQnaId=' + itemQnaId;
    });




});
