$(document).ready(function() {
    // Event delegation for dynamically generated buttons
    $(document).on('click', '.btn-addTracker', function() {
        const orderId = $(this).closest('tr').attr('id');
        const deliveryId = $(this).data('delivery-id');
        $('#modalOrderId').text(orderId);
        $('#deliveryId').val(deliveryId);
        $('#carrierId').val('');
        $('#trackId1').val('');
        $('#trackId2').val('');
        $('#trackId3').val('');



        // Hide update button and show save button
        $('#btn-updateTracker').hide();
        $('#btn-saveTracker').show();

        $('#trackerModal').modal('show');
    });

    $(document).on('click', '.btn-trackerInfo', function() {
        const orderId = $(this).closest('tr').attr('id');
        const deliveryId = $(this).data('delivery-id');
        const carrierId = $(this).data('carrier-id');
        const trackId = $(this).data('track-id').toString();

        $('#modalOrderId').text(orderId);
        $('#deliveryId').val(deliveryId);
        $('#carrierId').val(carrierId);

        $('#trackId1').val(trackId.substring(0, 4));
        $('#trackId2').val(trackId.substring(4, 8));
        $('#trackId3').val(trackId.substring(8, 12));

        // Show update button and hide save button
        $('#btn-updateTracker').show();
        $('#btn-saveTracker').hide();

        $('#trackerModal').modal('show');
    });

    // Modal close button
    $('.btn-close').click(function() {
        $('#trackerModal').modal('hide');
    });

    // Save button click
    $('#btn-saveTracker').click(function() {
        const trackId1 = $('#trackId1').val();
        const trackId2 = $('#trackId2').val();
        const trackId3 = $('#trackId3').val();

        const deliveryId = $('#deliveryId').val();
        const carrierId = $('#carrierId').val();
        const trackId = trackId1 + trackId2 + trackId3;

        // Create deliveryTrackerDto
        const deliveryTrackerDto = {
            deliveryId: deliveryId,
            carrierId: carrierId,
            trackId: trackId
        };

        console.log(deliveryTrackerDto);

        // AJAX request to save tracker info
        $.ajax({
            type: 'POST',
            url: '/api/ypjs/delivery/addTracker',
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
                console.error('오류가 발생했습니다.', error);
            }
        });
    });


});
