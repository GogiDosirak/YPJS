document.addEventListener("DOMContentLoaded", function() {
    var starRatingContainers = document.querySelectorAll("#starRating");

    starRatingContainers.forEach(function(starRatingContainer) {
        var rating = parseFloat(starRatingContainer.getAttribute("data-rating"));
        starRatingContainer.innerHTML = generateStarRating(rating);
    });

    // 별점을 생성하여 HTML 문자열 반환하는 함수 (순수 JavaScript)
    function generateStarRating(rating) {
        var starsHTML = '';
        var numStars;

        if (rating >= 0 && rating < 1) {
            numStars = 0;
        } else if (rating >= 1 && rating < 2) {
            numStars = 1;
        } else if (rating >= 2 && rating < 3) {
            numStars = 2;
        } else if (rating >= 3 && rating < 4) {
            numStars = 3;
        } else if (rating >= 4 && rating < 5) {
            numStars = 4;
        } else if (rating == 5) {
            numStars = 5;
        }

        for (var i = 0; i < numStars; i++) {
            starsHTML += '<i class="text-warning fa fa-star"></i>';
        }

        for (var i = numStars; i < 5; i++) {
            starsHTML += '<i class="text-muted fa fa-star"></i>';
        }

        return starsHTML;
    }
});