
let categoryBoardObject = {
    init: function() {
        let _this = this;

        $("#btn-categoryPost").on("click", function() {

            _this.insert();
        }),

        $("#btn-categoryUpdate").on("click", function() {

            _this.update();
        }),

        $("#btn-categoryDelete").on("click", function() {

            _this.delete(); // 수정된 부분: delete 함수 호출
        }),
        _this.categoryValues();
    },

     categoryValues: function() {
            // hidden input 요소의 값을 categoryParent select box에 설정
            let categoryParent = $("#hiddenCategoryParent").val();
            if (categoryParent != null) {
                $("#categoryParent").val(categoryParent.toString());
            }

            // hidden input 요소의 값을 categoryName select box에 설정
            let categoryName = $("#hiddenCategoryName").val();
            if (categoryName != null) {
                $("#categoryName").val(categoryName.toString());
            }
        },

    insert: function() {

       let categoryParent = $("#categoryParent").val();
       let categoryName = $("#categoryName").val();

       if (!categoryParent || categoryParent.trim().length === 0) {
           alert("카테고리 부모 번호를 입력하세요");
           return;
       }

       if(!categoryName || categoryName.trim().length === 0){
           alert("카테고리 이름을 입력하세요");
           return;
       }

        let data = {
            categoryParent: $("#categoryParent").val(),
            categoryName: $("#categoryName").val(),
        };

        $.ajax({
            type: "POST",
            url: "/api/ypjs/category/post",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
             alert("카테고리 저장되었습니다.");
                window.location.href = "/ypjs/category/get";
            },
            error: function(error) {
                           alert("에러 발생: " + JSON.stringify(error));
                       }
        });
    },

    update: function() {
        let categoryParent = $("#categoryParent").val();
        let categoryName = $("#categoryName").val();

        if (!categoryParent || categoryParent.trim().length === 0) {
            alert("카테고리 부모 번호를 입력하세요");
            return;
        }

        if (!categoryName || categoryName.trim().length === 0) {
            alert("카테고리 이름을 입력하세요");
            return;
        }

        let updateData = {
            categoryParent: categoryParent,
            categoryName: categoryName,
        };

        let categoryId = $("#categoryId").text(); // categoryId 가져오기

        $.ajax({
            type: "PUT",
            url: "/api/ypjs/category/update/" + categoryId,
            data: JSON.stringify(updateData),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                alert("카테고리가 수정되었습니다.");
                window.location.href = "/ypjs/category/get/" + categoryId;
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
            url: "/api/ypjs/category/delete/" + categoryId,
            success: function(response) {
                  alert("카테고리가 삭제되었습니다.");
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
