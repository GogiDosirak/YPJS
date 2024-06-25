
let categoryBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-categoryPost").on("click", function() {
            alert("카테고리 저장 버튼 클릭됨");
            _this.insert();
        }),

        $("#btn-categoryUpdate").on("click", function() {
            alert("수정이 요청되었습니다.");
            _this.update();
        }),

        $("#btn-categoryDelete").on("click", function() {
            alert("삭제가 요청되었습니다.");
            _this.delete(); // 수정된 부분: delete 함수 호출
        });
    },

    insert: function() {
        alert("카테고리 등록이 요청되었습니다.");

        let data = {
            categoryParent: $("#categoryParent").val(),
            categoryName: $("#categoryName").val(),
        };

        $.ajax({
            type: "POST",
            url: "/ypjs/category/post",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                window.location.href = "/ypjs/item/get";
            },
            error: function(error) {
                window.location.href = "/ypjs/item/get";
            }
        });
    },

    update: function() {
        let categoryId = $("#categoryId").val();

        let updateData = {
            categoryId: categoryId, // 수정된 부분: categoryId 추가
            categoryParent: $("#categoryParent").val(),
            categoryName: $("#categoryName").val(),
        };

        $.ajax({
            type: "PUT",
            url: "/ypjs/category/update/" + categoryId,
            data: JSON.stringify(updateData),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                window.location.href = "/ypjs/item/get";
            },
            error: function(error) {
                alert("에러 발생: " + JSON.stringify(error));
            }
        });
    },

    delete: function() {
        let categoryId = $("#categoryId").val(); // 삭제할 categoryId 가져오기

        $.ajax({
            type: "DELETE",
            url: "/ypjs/category/delete/" + categoryId,
            success: function(response) {

                window.location.href = "/ypjs/category/get"; // 삭제 후 페이지 이동
            },
            error: function(xhr, status, error) {
                alert("에러 발생: " + error);
            }
        });
    }
};

$(document).ready(function() {
    categoryBoardObject.init();
});
