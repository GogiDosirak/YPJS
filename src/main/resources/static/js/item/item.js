
let itemBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-itemDelete").on("click", function() {

            _this.delete();
        });

       $(document).on("click", ".btn-delete-item", function() {
           // 클릭된 버튼의 데이터 속성에서 itemId를 가져옴
            let itemId = $(this).data("itemid");
            // 아이템 삭제 함수 호출
             _this.deleteItem(itemId);
               });

         _this.categoryValues();
    },

     categoryValues: function() {
         // hidden input 요소의 값을 hiddenCategoryId select box에 설정
        let categoryId = $("#hiddenCategoryId").val();
         if (categoryId != null) {
                $("#categoryId").val(categoryId.toString());
            }
    },


    delete: function() {
        // itemId 가져오기
        let itemId = $("#itemId").val();

        $.ajax({
            type: "DELETE",
            url: "/api/ypjs/item/delete/" + itemId,
            success: function(response) {
                alert("삭제되었습니다.");
                window.location.href = "/ypjs/item/get"; // 삭제 후 페이지 이동
            },
            error: function(xhr, status, error) {
                alert("에러 발생: " + error);
            }
        });
    },

     deleteItem: function(itemId) {
     let categoryId = $("#categoryId").val(); // 삭제할 categoryId 가져오기
            $.ajax({
                type: "DELETE",
                url: "/api/ypjs/item/delete/" + itemId,
                success: function(response) {
                     alert("삭제되었습니다.");
                    window.location.href = "/ypjs/category/get/" + categoryId; // 삭제 후 페이지 이동
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
