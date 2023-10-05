// 주소 설정 완료 버튼 클릭시
function Address_commit() {}

var stompClient = null;

function onConnected() {
  console.log("연결 성공!");
  stompClient.subscribe("/sub/mozip/chat/invite", onMessageReceived);
}

function onMessageReceived(payload) {
  console.log("onMessage");
  var invite = JSON.parse(payload.body);
}

function onError() {
  console.log("연결 실패ㅜㅜ");
}

$(function () {
  //소켓 설정. 채팅 방에서 누군가가 초대를 보낼 때 알림 받기 위해 소켓 연결
  var socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);

  // 프로필 클릭시
  var $profile = $(".header-profile");
  var $layerProfile = $(".layer-header-profile");

  $profile
    .on("mouseenter", function (e) {
      e.preventDefault();
      $layerProfile.css({ left: "auto" }).fadeIn(100);
    })
    .on("mouseleave", function (e) {
      e.preventDefault();
      $layerProfile.css({ left: "auto" }).fadeOut(100);
    });

  $(".board-list").slice(0, 5).css("display", "block"); // 초기갯수
  $(".load-btn").click(function (e) {
    // 클릭시 more
    e.preventDefault();
    if ($(".board-list:hidden").length == 0) {
      // 컨텐츠 남아있는지 확인
      alert("게시물의 끝입니다."); // 컨텐츠 없을시 alert 창 띄우기
    }
    $(".board-list:hidden").slice(0, 5).css("display", "block"); // 클릭시 more 갯수 지정
  });
});

document.querySelector("#modal_close_btn").addEventListener("click", close);

//모집글 생성 버튼 클릭시
$(function () {
  function modalClose() {
    $("#modal").fadeOut();
  }

  $("#create").click(function () {
    // 생성 & DB 저장

    // 백앤드 화이팅

    modalClose(); // 모달 닫기 함수 호출
  });
  $(".v-bar").click(function () {
    $("#modal").css("display", "flex").hide().fadeIn(); // 속성 변경 후 hide로 숨기고 fadeIn으로 효과 나타내기
  });
  $("#close").click(function () {
    modalClose(); // 모달 닫기 함수 호출
  });
});

// selectbox js

/* ===== Logic for creating fake Select Boxes ===== */
$(".sel").each(function () {
  $(this).children("select").css("display", "none");

  var $current = $(this);

  $(this)
    .find("option")
    .each(function (i) {
      if (i == 0) {
        $current.prepend(
          $("<div>", {
            class: $current.attr("class").replace(/sel/g, "sel__box"),
          })
        );

        var placeholder = $(this).text();
        $current.prepend(
          $("<span>", {
            class: $current.attr("class").replace(/sel/g, "sel__placeholder"),
            text: placeholder,
            "data-placeholder": placeholder,
          })
        );

        return;
      }

      $current.children("div").append(
        $("<span>", {
          class: $current.attr("class").replace(/sel/g, "sel__box__options"),
          text: $(this).text(),
        })
      );
    });
});

// Toggling the `.active` state on the `.sel`.
$(".sel").click(function () {
  $(this).toggleClass("active");
});

// Toggling the `.selected` state on the options.
$(".sel__box__options").click(function () {
  var txt = $(this).text();
  var index = $(this).index();

  $(this).siblings(".sel__box__options").removeClass("selected");
  $(this).addClass("selected");

  var $currentSel = $(this).closest(".sel");
  $currentSel.children(".sel__placeholder").text(txt);
  $currentSel.children("select").prop("selectedIndex", index + 1);
});

//위에 header를 스크롤 할 시 header fixed로 바뀌며가 자동으로 고정되게 하기
const header = document.querySelector(".header");
var $topper = $(".topper");
var $window = $(window);

// 컨텐츠 영역부터 브라우저 최상단까지의 길이 구하기
const contentTop = header.getBoundingClientRect().top + window.scrollY;

window.addEventListener("scroll", function () {
  if ($window.scrollTop() > $topper.height()) {
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
  const newTop = row * rowHeight; //해당 해의 top 값이 615 증가

  // 새로운 left와 top 값을 설정
  element.style.left = `${newLeft}%`;
  element.style.top = `${newTop}px`;
  i++;
});
// ----------------------------------------

//카테고리 별 이미지
const imagePaths = {
  치킨: "images/categories/chicken.png",
  "피자/양식": "images/categories/pizza.png",
  중식: "images/categories/chines-food.png",
  한식: "images/categories/korean-food.png",
  "일식/돈까스": "images/categories/japan-food.png",
  "족발/보쌈": "images/categories/pighocks.png",
  고기: "images/categories/meet.png",
  분식: "images/categories/BoonSick.png",
  "카페/디저트": "images/categories/Cafe.png",
  아시안: "images/categories/Vietnamese-food.png",
  샌드위치: "images/categories/Sandwich.png",
  셀러드: "images/categories/Salad.png",
  "도시락/죽": "images/categories/redbead.png",

  // 나머지 카테고리도 추가해주세요
};

const boardList = document.querySelector(".board-list"); //큰 부모 div boardList 게시물 목록 선택하기 위한 큰 목록
const boardElements = boardList.querySelectorAll(".board"); //boardList 게시물 목록안에 든 게시글 배열로 생성

boardElements.forEach((boardElement) => {
  //배열을 하나씩 돈다(게시물 하나씩 돔) boardElement는 객체1,,,2 란 변수 이름 for i in range(10)에서 i라고 생각하면 됨
  const category = boardElement.querySelector(".board-categories").textContent; //데이터베이스에 넣어둔 categories의 텍스트 값을 html에서 가져옴
  const imagePath = imagePaths[category]; //위 imagePaths에 저장한 이미지들과 categories의 텍스트 값을 이미지에 맞게 매핑시켜 줬으므로 같은 이름이면 해당하는 이미지 경로를 imagePaths 배열에서 가져옴.
  const imgElement = boardElement.querySelector(".board-img"); //img 경로를 바꿔주기 위해
  imgElement.src = imagePath; //img 클래스의 src 경로를 해당하는 categories로 설정해줌
});


