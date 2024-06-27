$(document).ready(function() {
    var initialScore = $('#itemScore').val(); // 기존의 itemScore 값 가져오기
    $('#starRating').html(generateStarRating(initialScore)); // 초기화: 기존의 별점으로 별 아이콘 표시

    // 별 아이콘 클릭 이벤트 리스너 추가
    $('#starRating').on('click', '.fa-star', function() {
        var score = $(this).data('score');
        $('#itemScore').val(score); // hidden 필드에 새로운 점수 설정
        $('#starRating').html(generateStarRating(score)); // 별 아이콘 업데이트
    });

    // 별 아이콘을 생성하여 HTML 문자열 반환하는 함수
    function generateStarRating(score) {
        var starsHTML = '';
        for (var i = 1; i <= 5; i++) {
            if (i <= score) {
                starsHTML += '<i class="fas fa-star rating-stars filled-star" data-score="' + i + '"></i>';
            } else {
                starsHTML += '<i class="fas fa-star rating-stars" data-score="' + i + '"></i>';
            }
        }
        return starsHTML;
    }
});



