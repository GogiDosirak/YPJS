$(document).ready(function() {
    // 모달 열기
    $('#btn-addTracker').click(function() {
        const orderId = $('#orderId').val();
        const deliveryId = $(this).data('delivery-id');
        $('#modalOrderId').text(orderId);
        $('#deliveryId').val(deliveryId);
        $('#carrierId').val();

        // 수정 버튼 숨기기
        $('#btn-updateTracker').hide();
        // 저장 버튼 보이기
        $('#btn-saveTracker').show();

        $('#trackerModal').modal('show');
    });


    $('#btn-trackerInfo').click(function() {
        const orderId = $('#orderId').val();
        const deliveryId = $(this).data('delivery-id');
        const carrierId = $(this).data('carrier-id');
        const trackId = $(this).data('track-id').toString();  // trackId를 jQuery data() 메서드로 가져옵니다.

        $('#modalOrderId').text(orderId);
        $('#deliveryId').val(deliveryId);
        $('#carrierId').val(carrierId);

        $('#trackId1').val(trackId.substring(0, 4));
        $('#trackId2').val(trackId.substring(4, 8));
        $('#trackId3').val(trackId.substring(8, 12));

        // 수정 버튼 보이기
        $('#btn-updateTracker').show();
        // 저장 버튼 숨기기
        $('#btn-saveTracker').hide();

        $('#trackerModal').modal('show');
    });


    // 모달 닫기
    $('.btn-close').click(function() {
        $('#trackerModal').modal('hide');
    });

    // 저장 버튼 클릭
    $('#btn-saveTracker').click(function() {
        var trackId1 = $('#trackId1').val();
        var trackId2 = $('#trackId2').val();
        var trackId3 = $('#trackId3').val();

        var deliveryId = $('#deliveryId').val();
        var carrierId = $('#carrierId').val();
        var trackId = trackId1 + trackId2 + trackId3;


        // 추가적으로 저장 작업을 여기서 수행합니다.
        console.log('배송 ID:', deliveryId);
        console.log('배송회사:', carrierId);
        console.log('운송장번호:', trackId);


        var deliveryTrackerDto = {
            deliveryId: deliveryId,
            carrierId: carrierId,
            trackId: trackId
        };

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/ypjs/delivery/addTracker',
            contentType: 'application/json',
            data: JSON.stringify(deliveryTrackerDto),
            success: function(response) {
                Swal.fire({
                    icon: "success",
                    iconColor: '#007bff',
                    confirmButtonColor: '#007bff',
                    text: '배송정보가 등록되었습니다.',
                    showCloseButton: true
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.reload();
                    }
                });
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 처리
                console.error('오류가 발생했습니다.', error);
            }
        });
    });


});
