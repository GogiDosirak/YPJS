
let itemBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-itemReviewPost").on("click", function() {
            _this.insert();
        }),
        $("#btn-itemReviewUpdate").on("click", function() {

              _this.update();
                }),

        $(document).on("click", ".btn-delete-itemReview", function() {
          // 클릭된 버튼의 데이터 속성에서 itemReviewId를 가져옴
          let itemReviewId = $(this).data("itemreviewid");
          // 아이템 리뷰 삭제 함수 호출
           _this.deleteItemReview(itemReviewId);
             });


        // Summernote 초기화
          $("#itemReviewContent").summernote({
            height: 300,
            placeholder: '내용을 입력하세요...'
            });



    },

    insert: function() {


        let itemId = $("#itemId").val();  // URL에 사용할 itemId를 가져옴
        let itemScore = $("#itemScore").val(); // 별점 가져오기
        let itemReviewName = $("#itemReviewName").val(); // 제목 가져오기
        let itemReviewContent = $("#itemReviewContent").summernote('code'); //썸머노트 내용 가져오기


                // 별점을 선택하지 않은 경우 처리
                if (itemScore === "0") {
                    alert("별점을 선택하세요.");
                    return; // 리뷰 등록 중단
                }

                // 제목이 비어 있는지 확인
                 if (!itemReviewName || itemReviewName.trim().length === 0) {
                      alert("제목을 입력하세요.");
                      return; // 리뷰 등록 중단
                  }

                // 썸머노트에서 가져온 HTML 태그 제거 및 내용 검사
                 let plainTextContent = stripTags(itemReviewContent).trim();

                    if (!plainTextContent || plainTextContent.length === 0) {
                        alert("내용을 입력하세요.");
                        return;
                    }



        let data = {
            itemId: itemId,  // itemId를 데이터에 포함
            memberId: $("#memberId").val(),  // 멤버 ID 추가
            itemReviewName: $("#itemReviewName").val(),
            itemScore: $("#itemScore").val(),
            itemReviewContent: $("#itemReviewContent").val(),
        };

        $.ajax({
            type: "POST",
            url: "/api/ypjs/itemReview/post/" + itemId,  // URL에 itemId를 추가
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                alert("리뷰가 등록되었습니다.");
                window.location.href = "/ypjs/itemReview/get/" + itemId;  // 성공 후 리디렉션
            },
            error: function(error) {
                alert("에러 발생: " + JSON.stringify(error));
            }
        });
    },


     update: function() {

        let itemReviewId = $("#itemReviewId").val();
        let itemScore = $("#itemScore").val(); // 별점 가져오기
        let itemReviewName = $("#itemReviewName").val(); // 제목 가져오기
        let itemReviewContent = $("#itemReviewContent").summernote('code'); //썸머노트 내용 가져오기


          // 별점을 선택하지 않은 경우 처리
           if (itemScore === "0") {
               alert("별점을 선택하세요.");
               return; // 리뷰 등록 중단
           }

         // 제목이 비어 있는지 확인
          if (!itemReviewName || itemReviewName.trim().length === 0) {
               alert("제목을 입력하세요.");
               return; // 리뷰 등록 중단
          }

          // 썸머노트에서 가져온 HTML 태그 제거 및 내용 검사
         let plainTextContent = stripTags(itemReviewContent).trim();
          if (!plainTextContent || plainTextContent.length === 0) {
              alert("내용을 입력하세요.");
               return;
                            }



            let updateData = {

                itemReviewName: $("#itemReviewName").val(),
                itemScore: $("#itemScore").val(),
                itemReviewContent: $("#itemReviewContent").val(),
            };

            $.ajax({
                type: "PUT",

                url: "/api/ypjs/itemReview/update/" + itemReviewId,
                data: JSON.stringify(updateData),
                contentType: "application/json; charset=utf-8",
                success: function(response) {
                alert("리뷰가 수정되었습니다.");
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
                     url: "/api/ypjs/itemReview/delete/" + itemReviewId,
                     success: function(response) {
                         alert("리뷰가 삭제되었습니다.");

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



// HTML 태그 제거 함수
function stripTags(html) {
    let tmp = document.createElement("DIV");
    tmp.innerHTML = html;
    return tmp.textContent || tmp.innerText || "";
}

