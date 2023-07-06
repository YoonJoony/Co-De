// 주소 설정 완료 버튼 클릭시
function Address_commit() {}

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

// 모집글 페이지
const contents = document.querySelector(".contents");
const buttons = document.querySelector(".buttons");

const numOfContent = 178;
const showContent = 10;
const showButton = 5;
const maxPage = Math.ceil(numOfContent / maxContent);
let page = 1;

const makeContent = (id) => {
  const content = document.createElement("li");
  content.classList.add("content");
  content.innerHTML = `
    <span class="content__id">${id}</span>
    <span class="content__title">게시물 제목</span>
    <span class="content__author">작성자</span>
    <span class="content__date">2022.01.01</span>
    `;
  return content;
};

const makeButton = (id) => {
  const button = document.createElement("button");
  button.classList.add("button");
  button.dataset.num = id;
  button.innerText = id;
  button.addEventListener("click", (e) => {
    Array.prototype.forEach.call(buttons.children, (button) => {
      if (button.dataset.num) button.classList.remove("active");
    });
    e.target.classList.add("active");
    renderContent(parseInt(e.target.dataset.num));
  });
  return button;
};

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
