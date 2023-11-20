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

//내 채팅 누를 시 내 채팅방 이동
async function myChat() {
  try {
    const response = await $.ajax({
      type: "GET",
      url: "/mozip/chat/myChatroom",
      data: {},
    });
    location.href = "/mozip/chat/room?id=" + response;
  } catch (error) {
    console.log("내 채팅 접속 요청 실패");
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

  // 별점 초기화
  $(".starRev span").parent().children("span").removeClass("on");
  // 별점 점수 텍스트 없애기
  document.querySelector("#star_num_text").innerHTML = null;
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

//별점 클릭 시 별점 변화
$(".starRev span").click(function () {
  $(this).parent().children("span").removeClass("on");
  $(this).addClass("on").prevAll("span").addClass("on");

  // starRev span 개수로 별점 점수 출력하기
  var star_count = document.querySelectorAll(".starR.on");
  document.querySelector("#star_num_text").innerHTML = star_count.length + "점";

  return false;
});

//위에 header를 스크롤 할 시 header fixed로 바뀌며가 자동으로 고정되게 하기
const header = document.querySelector(".header");

// 컨텐츠 영역부터 브라우저 최상단까지의 길이 구하기
const contentTop = header.getBoundingClientRect().top + window.scrollY;

window.addEventListener("scroll", function () {
  if (window.scrollY >= contentTop) {
    header.classList.add("fixed");
  } else {
    header.classList.remove("fixed");
  }
});
// ----------------------------------------

//모집글 board 영역 설정
const elements = document.querySelectorAll(".board");
const rowHeight = 615.56;
var i = 0;
elements.forEach((element, index) => {
  const row = Math.floor(index / 4); //현재 요소가 속한 행 번호

  var left = parseFloat(element.style.left); //left값을 받아온다. 처음에는 0. 두번째는 24.9617
  //const top = parseFloat(element.style.top);

  // 비율에 맞게 조정

  if (i % 4 == 0) {
    i = 0;
  }

  const newLeft = i * 25;
  const newTop = row * rowHeight; //해당 해의 top 값이 223씩 증가

  // 새로운 left와 top 값을 설정
  element.style.left = `${newLeft}%`;
  element.style.top = `${newTop}px`;
  i++;
});
// ----------------------------------------
