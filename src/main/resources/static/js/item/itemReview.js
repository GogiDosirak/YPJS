//let itemBoardObject = {
//    init: function() {
//        let _this = this;
//
//        $("#btn-itemReviewPost").on("click", function() {
//            alert("리뷰등록 버튼이 클릭 됨");
//            _this.insert();
//        });
//    },
//
//    insert: function() {
//        alert("리뷰 등록이 요청되었습니다.");
//
//        let itemId = $("#itemId").val();  // URL에 사용할 itemId를 가져옴
//
//        let data = {
//            itemReviewName: $("#itemReviewName").val(),
//            itemScore: $("#itemScore").val(),
//            itemReviewContent: $("#itemReviewContent").val(),
//        };
//
//        $.ajax({
//            type: "POST",
//            url: "/ypjs/itemReview/post/" + itemId,  // URL에 itemId를 추가
//            data: JSON.stringify(data),
//            contentType: "application/json; charset=utf-8",
//            success: function(response) {
//                alert("리뷰가 성공적으로 등록되었습니다.");
//                window.location.href = "/ypjs/item/get/" + itemId;  // 성공 후 리디렉션
//            },
//            error: function(error) {
//                alert("에러 발생: " + JSON.stringify(error));
//            }
//        });
//    }
//};
//
//$(document).ready(function() {
//    itemBoardObject.init();
//});
let itemBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-itemReviewPost").on("click", function() {
            alert("리뷰등록 버튼이 클릭 됨");
            _this.insert();
        });
    },

    insert: function() {
        alert("리뷰 등록이 요청되었습니다.");

        let itemId = $("#itemId").val();  // URL에 사용할 itemId를 가져옴

        let data = {
            itemId: itemId,  // itemId를 데이터에 포함
            memberId: $("#memberId").val(),  // 멤버 ID 추가
            itemReviewName: $("#itemReviewName").val(),
            itemScore: $("#itemScore").val(),
            itemReviewContent: $("#itemReviewContent").val(),
        };

        $.ajax({
            type: "POST",
            url: "/ypjs/itemReview/post/" + itemId,  // URL에 itemId를 추가
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                alert("리뷰가 성공적으로 등록되었습니다.");
                window.location.href = "/ypjs/item/get/" + itemId;  // 성공 후 리디렉션
            },
            error: function(error) {
                alert("에러 발생: " + JSON.stringify(error));
            }
        });
    }
};

$(document).ready(function() {
    itemBoardObject.init();
});
