 $(document).ready(function() {

            $('#like-button').click(function() {
//                var itemId = $(this).data('itemId');
//                var memberId = $('#memberId').text();
//todo: 하드코딩된 값 바꿔치기 해줘야함
                var itemId = 1;
                var memberId = 1;
                var likeRequestDto = {
                    itemId: parseInt(itemId),
                    memberId: parseInt(memberId)
                };
                $.ajax({
                    type: 'POST',
                    url: '/api/ypjs/like/post',
                    contentType: 'application/json',
                    data: JSON.stringify(likeRequestDto),
                    success: function(response) {
                        if(response){
                        $('#like-button').removeClass('far').addClass('fas');
                        }else{
                        $('#like-button').removeClass('fas').addClass('far');
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#likeStatus').text('좋아요 처리 중 오류 발생');
                        console.error('좋아요 처리 중 오류 발생:', error);
                    }
                });
            });
        });