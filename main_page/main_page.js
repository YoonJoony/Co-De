// 주소 설정 완료 버튼 클릭시
function Address_commit() {}

// 프로필 클릭시
function profile_btn() {
  const box = document.getElementById("layer-header-profile");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}

$(function () {
  $(".content-list").slice(0, 5).css("display", "block"); // 초기갯수
  $(".load-btn").click(function (e) {
    // 클릭시 more
    e.preventDefault();
    if ($(".content-list:hidden").length == 0) {
      // 컨텐츠 남아있는지 확인
      alert("게시물의 끝입니다."); // 컨텐츠 없을시 alert 창 띄우기
    }
    $(".content-list:hidden").slice(0, 5).css("display", "block"); // 클릭시 more 갯수 지정
  });
});

// 마이페이지로 버튼 클릭시 마이페이지로 이동
function myPage() {}

// 모집글 생성 버튼 클릭시
function make_content() {}

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

$(function () {
  $(".title").click(function () {
    $(".modal-overlay").css("display", "flex");
    if ($(".modal-overlay").display == "none") {
      $(".modal-overlay").show();
    }
  });
});
