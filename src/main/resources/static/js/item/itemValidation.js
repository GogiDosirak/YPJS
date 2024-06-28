$(document).ready(function () {
    // Summernote 초기화
    $("#itemContent").summernote({
        height: 500,
        placeholder: '상품 내용을 입력하세요...',
        callbacks: {
            onChange: function (contents, $editable) {
                // 내용이 변경될 때 추가 로직
            }
        }
    });

    // 파일명 표시 함수
    function displayFileName(input) {
        if (input.files.length > 0) {
            var fileName = input.files[0].name;
            document.getElementById('file-name-display').innerText = '선택된 파일: ' + fileName;
        } else {
            document.getElementById('file-name-display').innerText = '';
        }
    }

    // 폼 제출 전 유효성 검사
    $('#itemForm').submit(function (event) {
        var categoryId = $("#categoryId").val();
        var itemName = $("#itemName").val();
        var itemPrice = $("#itemPrice").val();
        var itemStock = $("#itemStock").val();
        var itemContent = $("#itemContent").summernote('code');

        if (!categoryId || categoryId.trim().length === 0) {
            alert('카테고리 번호를 입력하세요.');
            event.preventDefault(); // 폼 제출 방지
            return;
        }

        if (!itemName || itemName.trim().length === 0) {
            alert('상품명을 입력하세요.');
            event.preventDefault(); // 폼 제출 방지
            return;
        }

        if (!itemPrice || itemPrice.trim().length === 0) {
            alert('상품가격을 입력하세요.');
            event.preventDefault(); // 폼 제출 방지
            return;
        }

        if (!itemStock || itemStock.trim().length === 0) {
            alert('상품수량을 입력하세요.');
            event.preventDefault(); // 폼 제출 방지
            return;
        }

         // 썸머노트에서 가져온 HTML 태그 제거 및 내용 검사
         let plainTextContent = stripTags(itemContent).trim();

        if (!plainTextContent || plainTextContent.trim().length === 0) {
            alert('상품내용을 입력하세요.');
            event.preventDefault(); // 폼 제출 방지
            return;
        }

        var fileInput = document.getElementById('file');
        if (fileInput.files.length === 0) {
            alert('썸네일 파일을 선택하세요.');
            event.preventDefault(); // 폼 제출 방지
        }
    });
});


// HTML 태그 제거 함수
function stripTags(html) {
    let tmp = document.createElement("DIV");
    tmp.innerHTML = html;
    return tmp.textContent || tmp.innerText || "";
}