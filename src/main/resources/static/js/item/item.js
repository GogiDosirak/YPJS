
let itemBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-itemPost").on("click", function() {
            alert("상품 저장 버튼 클릭됨");
            _this.insert();
        });

        $("#btn-itemDelete").on("click", () => {
            _this.delete();
        });

        $(document).on("click", ".btn-delete-item", function() {
            // 클릭된 버튼의 데이터 속성에서 itemId를 가져옴
            let itemId = $(this).data("itemid");

            // 아이템 삭제 함수 호출
            _this.deleteItem(itemId);
        });
    },

    insert: function() {
        alert("글 등록이 요청되었습니다.");

        let data = {
            categoryId: $("#categoryId").val(),
            itemName: $("#itemName").val(),
            itemPrice: $("#itemPrice").val(),
            itemStock: $("#itemStock").val(),
            itemContent: $("#summernote").summernote('code') // Summernote의 HTML 내용 가져오기
        };

        $.ajax({
            type: "POST",
            url: "/ypjs/item/post",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            enctype: 'multipart/form-data', // 추가
            success: function(response) {
                window.location.href = "/test";
            },
            error: function(error) {
                alert("에러 발생: " + JSON.stringify(error));
            }
        });
    },

    delete: function() {
        // itemId 가져오기
        let itemId = $("#itemId").val();

        $.ajax({
            type: "DELETE",
            url: "/ypjs/item/delete/" + itemId,
            success: function(response) {
                alert(response.data); // 성공 메시지 처리
                window.location.href = "/test"; // 삭제 후 페이지 이동
            },
            error: function(xhr, status, error) {
                alert("에러 발생: " + error);
            }
        });
    },

    deleteItem: function(itemId) {
        $.ajax({
            type: "DELETE",
            url: "/ypjs/item/delete/" + itemId,
            success: function(response) {
                alert(response.data); // 성공 메시지 처리
                window.location.href = "/test"; // 삭제 후 페이지 이동
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
