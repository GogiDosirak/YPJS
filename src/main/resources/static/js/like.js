 $(document).ready(function() {

            //새로고침 시 스크롤 위치 복원용.
            const savedScrollTop = sessionStorage.getItem('scrollTop');
            if (savedScrollTop) {
                $(window).scrollTop(savedScrollTop);
            }

            $('.like-button').click(function(event) {

                event.preventDefault();

                var itemId = $(this).data('item-id');
                var memberId = $('#memberId').val();

                // 값을 콘솔에 출력하여 확인합니다.
                console.log('itemId:', itemId);  // data-itemId의 값 확인
                console.log('memberId:', memberId);  // memberId의 값 확인

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
                        alert("찜하기를 했습니다.")
                        }else{
                        alert("찜하기를 취소했습니다.")
                        }

                    // 페이지 새로 고침 전 스크롤 위치를 저장
                    sessionStorage.setItem('scrollTop', $(window).scrollTop());

                    //새로고침 후 좋아요 취소 반영
                    location.reload()
                    },
                    error: function(xhr, status, error) {
                        console.error('좋아요 처리 중 오류 발생:', error);
                    }
                });
            });
        });