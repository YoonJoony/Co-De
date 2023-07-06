// 주소 설정 완료 버튼 클릭시
function Address_commit(){
    
}

function profile_btn(){
    const box = document.getElementById("layer-header-profile")
    
    // btn1 숨기기 (display: none)
    if(box.style.display !== 'block') {
	    box.style.display = 'block';
	}
	  // btn` 보이기 (display: block)
	else {
	    box.style.display = 'none';
	}
}

//글 더보기 버튼
load_btn_click(){
    $('.load-btn')
}

// 마이페이지로 버튼 클릭시 마이페이지로 이동
function myPage(){

}

// // 현재 위치 가져오기
// navigator.geolocation.getCurrentPosition(getSuccess, getError);

// // 가져오기 성공
// function getSuccess(position) {
//   // 위도
//     const lat = position.coords.latitude;
//   // 경도
//     const lng = position.coords.logitude;

//     document.getElementById("address_input").value = lat;
// }

// // 가지오기 실패(거부)
// function getError() {
//     alert('Geolocation Error');
// }