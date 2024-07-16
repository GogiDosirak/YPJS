$(document).ready(function() {
    // 배송 조회 버튼 클릭 시 이벤트 처리
    $('#delivery-tracker-modal').click(function() {
        const deliveryId = parseInt($('#delivery-id').val()); // hidden input에서 deliveryId 가져오기
        console.log(deliveryId);

        // AJAX 요청
        $.ajax({
            url: '/ypjs/delivery/tracker',
            type: 'GET',
            data: {'deliveryId': deliveryId},
            contentType: 'application/json', // 요청의 Content-Type을 JSON으로 설정
            success: function(response) {
                // 성공 시 실행될 코드
                console.log(response); // 받은 데이터를 콘솔에 출력
                const trackLog = JSON.parse(response);

                // trackLog 데이터를 hidden input에 저장
                $('#trackLogData').val(JSON.stringify(trackLog));

                // 모달 열기
                $('#deliveryTrackerModal').modal('show');
            },
            error: function(xhr, status, error) {
                // 실패 시 실행될 코드
                console.error('Error:', error);
            }
        });
    });

    // 모달이 열릴 때 이벤트 처리
    $('#deliveryTrackerModal').on('show.bs.modal', function (event) {
        const trackLogData = $('#trackLogData').val();
        if (trackLogData) {
            const trackLog = JSON.parse(trackLogData);
            if (trackLog.progresses && trackLog.progresses.length > 0) {
                let trackLogHtml = `
                    <div class="card">
                        <div class="card-body bg-success text-white">
                            <h5 class="card-title mb-4 fw-bold">운송장 번호</h5>
                            <p class="card-text fw-bold">${trackLog.trackId}</p>
                            <p class="card-text fw-bold">${trackLog.carrierName}</p>
                        </div>
                    </div>
                    <div class="progress-icons d-flex align-content-center justify-content-between p-5">
                        <div class="text-center">
                            <i class="fas fa-box fa-2x" style="color: ${trackLog.status === 'at_pickup' ? '#74a974' : ''};"></i>
                            <p class="mt-2 mb-0 fw-bold" style="color: ${trackLog.status === 'at_pickup' ? '#74a974' : ''};">상품인수</p>
                        </div>
                        <div class="text-center">
                            <i class="fas fa-truck-moving fa-2x" style="color: ${trackLog.status === 'in_transit' ? '#74a974' : ''};"></i>
                            <p class="mt-2 mb-0 fw-bold" style="color: ${trackLog.status === 'in_transit' ? '#74a974' : ''};">상품이동중</p>
                        </div>
                        <div class="text-center">
                            <i class="fas fa-warehouse fa-2x" style="color: ${trackLog.status === '-' ? '#74a974' : ''};"></i>
                            <p class="mt-2 mb-0 fw-bold" style="color: ${trackLog.status === '-' ? '#74a974' : ''};">배송지도착</p>
                        </div>
                        <div class="text-center">
                            <i class="fas fa-shipping-fast fa-2x" style="color: ${trackLog.status === 'out_for_delivery' ? '#74a974' : ''};"></i>
                            <p class="mt-2 mb-0 fw-bold" style="color: ${trackLog.status === 'out_for_delivery' ? '#74a974' : ''};">배송출발</p>
                        </div>
                        <div class="text-center">
                            <i class="fas fa-check-circle fa-2x" style="color: ${trackLog.status === 'delivered' ? '#74a974' : ''};"></i>
                            <p class="mt-2 mb-0 fw-bold" style="color: ${trackLog.status === 'delivered' ? '#74a974' : ''};">배송완료</p>
                        </div>
                    </div>
                    <div class="timeline">`;

                trackLog.progresses.forEach(function(progress) {
                    const iconColor = progress.status.text === trackLog.status ? '#74a974' : '#ffffff';
                    trackLogHtml += `
                        <div class="timeline-item">
                                <div class="timeline-icon"><i class="fas fa-circle" style="color:${iconColor}; margin-top: 1.2px; margin-right: 0.2px;"></i></div>
                                <div class="timeline-content">
                                    <p class="location mb-0">${progress.location.name} <span> | </span> <span class="status">${progress.status.text}</span></p>
                                    <p class="time">${new Date(progress.time).toLocaleString()}</p>
                                </div>
                        </div>`;
                });

                trackLogHtml += `</div>`;

                $('#trackLogContent').html(trackLogHtml);
            } else {
                $('#trackLogContent').html('<p>배송 준비중 입니다.</p>');
            }
        }
    });
});
