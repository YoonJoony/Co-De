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

// 모달창 보여주는 함수
function show_modal() {
  document.querySelector(".modal-background").className =
    "modal-background show-modal";
}
// 호스트 평가 버튼 클릭 시 show_modal 함수 호출
var star_rating_btns = document.querySelectorAll("#star_rating_btn");
// 반복분으로 모든 id에 이벤트 저장
for (var i = 0; i < star_rating_btns.length; i++) {
  star_rating_btns[i].addEventListener("click", show_modal);
}

// 모달 창 닫기
function close_modal() {
  document.querySelector(".modal-background").className = "modal-background";
}
// x클릭 시 close_modal 함수 호출
document
  .querySelector(".modal-popup-close")
  .addEventListener("click", close_modal);

// 확인 버튼 클릭 시 close_modal 함수 호출
var modal_commit_btns = document.querySelectorAll("#modal_commit");
// 반복문
for (var i = 0; i < modal_commit_btns.length; i++) {
  modal_commit_btns[i].addEventListener("click", close_modal);
}

//별점 클릭 시
$(".starRev span").click(function () {
  $(this).parent().children("span").removeClass("on");
  $(this).addClass("on").prevAll("span").addClass("on");

  // starRev span 개수로 별점 점수 출력하기
  var star_count = document.querySelectorAll(".starRev span");
  console.log(star_count.length);

  return false;
});
