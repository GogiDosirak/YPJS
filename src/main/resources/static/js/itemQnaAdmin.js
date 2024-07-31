$(document).ready(function() {

/** listAdmin.html **/

//목록에서 행 클릭하면 상세페이지로 이동
    $('tbody').on('click', 'tr', function() {
        var itemQnaId = $(this).attr('id');
        location = '/ypjs/itemqna/detail/admin?itemQnaId=' + itemQnaId;
    });

/** detailAdmin.html **/
    $('#btn-answer').click(function() {
        var itemQnaId = $('#itemQnaId').val();
        location = '/ypjs/itemqna/answer?itemQnaId=' + itemQnaId;
    });

/** answer.html **/
    $('#btn-submit').click(function() {
        var itemQnaId = $('#itemQnaId').val();
        var memberId = $('#aMemberId').val();
        var answer = $('#answer').val();

        console.log(itemQnaId);
        console.log(memberId);
        console.log(answer);

        var ItemQnaAnswerDto = {
            itemQnaId: itemQnaId,
            memberId: memberId,
            answer: answer
        }

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/api/ypjs/itemqna/answer',
            contentType: 'application/json',
            data: JSON.stringify(ItemQnaAnswerDto),
            success: function(response) {
                console.log('상품문의 답변 완료');
                Swal.fire({
                    text: "답변이 등록되었습니다.",
                    confirmButtonColor: '#007bff',
                    iconColor: '#007bff',
                    icon: "success"
                }).then((result) => {
                    if (result.isConfirmed) {
                        //상품 상세로 이동
                        location = '/ypjs/itemqna/detail/admin?itemQnaId=' + itemQnaId;
                    }
                });
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 처리
                console.error('오류 발생:', error);
            }
        });
    });

});
