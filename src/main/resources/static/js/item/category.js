
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
          $("#btn-categoryDelete").on("click", () => {
              alert("삭제가 요청되었습니다.");
                    _this.delete();
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
            categoryId: $("#categoryId").val(),
            categoryParent: $("#categoryParent").val(),
            categoryName: $("#categoryName").val(),
        };

        $.ajax({
            type: "PUT",
            // URL 경로 조합 시, 경로와 id 값을 올바르게 분리하고 '/'를 추가합니다.
            url: "/ypjs/category/update/" + categoryId,
            data: JSON.stringify(updateData),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                window.location.href = "/ypjs/item/get";
            },
            error: function(error) {
                alert("에러 발생: " + JSON.stringify(error));
                // 에러 발생 시 적절히 처리하도록 수정이 필요할 수 있습니다.
            }
        });
    },
     delete: function() {
            // itemId 가져오기
            let categoryId = $("#categoryId").val();

            $.ajax({
                type: "DELETE",
                url: "/ypjs/category/delete/" + categoryId,
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
    categoryBoardObject.init();
});
