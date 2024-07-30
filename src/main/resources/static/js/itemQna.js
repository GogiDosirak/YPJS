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
                        location = "/ypjs/item/get/" + response;
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

/** itemGet.html **/

//문의 없는 경우 문의 li 클릭시
    $('#btn-itemQna').click(function() {
        var itemId = $('#itemId').val();
        console.log(itemId);

        Swal.fire({
            title: '등록된 문의가 없습니다.',
            text: '문의를 작성하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#007bff',
            confirmButtonText: '작성하기',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                location = "/ypjs/itemqna/create?itemId=" + itemId;
            } else {
                location.reload();
            }
        });

    });


});
