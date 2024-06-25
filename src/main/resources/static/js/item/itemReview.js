
let itemBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-itemReviewPost").on("click", function() {
            alert("리뷰등록 버튼이 클릭 됨");
            _this.insert();
        }),
         $("#btn-itemReviewUpdate").on("click", function() {
                    alert("수정이 요청되었습니다.");
                    _this.update();
                }),

         $(document).on("click", ".btn-delete-itemReview", function() {
                   // 클릭된 버튼의 데이터 속성에서 itemReviewId를 가져옴
                   let itemReviewId = $(this).data("itemreviewid");
                   alert("삭제가 요청되었습니다.");
                    // 아이템 리뷰 삭제 함수 호출
                   _this.deleteItemReview(itemReviewId);
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
    },


     update: function() {

        let itemReviewId = $("#itemReviewId").val();


            let updateData = {

                itemReviewName: $("#itemReviewName").val(),
                itemScore: $("#itemScore").val(),
                itemReviewContent: $("#itemReviewContent").val(),
            };

            $.ajax({
                type: "PUT",

                url: "/ypjs/itemReview/update/" + itemReviewId,
                data: JSON.stringify(updateData),
                contentType: "application/json; charset=utf-8",
                success: function(response) {
                window.location.href = "/ypjs/itemReview/get/" + response ;

                },
                error: function(error) {
                    alert("에러 발생: " + JSON.stringify(error));
                    // 에러 발생 시 적절히 처리하도록 수정이 필요할 수 있습니다.
                }
            });
        },




         deleteItemReview: function(itemReviewId) {

                 let itemId = $("#itemId").val();

                 $.ajax({
                     type: "DELETE",
                     url: "/ypjs/itemReview/delete/" + itemReviewId,
                     success: function(response) {
                         alert("리뷰가 성공적으로 삭제되었습니다.");

                         window.location.href = "/ypjs/itemReview/get/" + itemId;
                     },
                     error: function(xhr, status, error) {
                         alert("에러 발생: " + error);
                     }
                 });
             }


        };



$(document).ready(function() {
    itemBoardObject.init();
});




