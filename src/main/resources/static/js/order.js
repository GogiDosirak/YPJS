// 모달 관련 JavaScript
var modal = document.getElementById('deliveryModal');
var btn = document.getElementById('openDeliveryModal');
var span = document.getElementsByClassName('close')[0];

btn.onclick = function () {
    modal.style.display = 'block';
}

span.onclick = function () {
    modal.style.display = 'none';
}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}

// Save 버튼 클릭 시, 모달에 입력된 값들을 기존 화면에 텍스트로 나타내기
document.getElementById('saveDeliveryInfo').addEventListener('click', function () {
    document.getElementById('receiver').innerText = document.getElementById('modalReceiver').value;
    document.getElementById('phoneNumber').innerText = document.getElementById('modalPhoneNumber').value;
    document.getElementById('address').innerText =  document.getElementById('modalAddress').value;
    document.getElementById('addressDetail').innerText = document.getElementById('modalAddressDetail').value;
    document.getElementById('zipcode').innerText = document.getElementById('modalZipcode').value;

    // 모달 닫기
    modal.style.display = 'none';
});
