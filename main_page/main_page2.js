// 주소 설정 완료 버튼 클릭시
function Address_commit() {}

$(function () {
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

// 마이페이지로 버튼 클릭시 마이페이지로 이동
function myPage() {}

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

//let el = document.querySelectorAll(".board-button");
//el.forEach((target) => target.addEventListener("click", show));
//
//function show() {
//  document.querySelector(".modal-background").className =
//    "modal-background show";
//}
//
//function close() {
//  document.querySelector(".modal-background").className = "modal-background";
//}

document.querySelector("#modal_close_btn").addEventListener("click", close);

// 모집글 생성 버튼 클릭시
$(function () {
  function modalClose() {
    $("#modal").fadeOut();
  }

  $("#create").click(function () {
    // 생성 & DB 저장

    // 백앤드 화이팅

    modalClose(); // 모달 닫기 함수 호출
  });
  $("#createOpen").click(function () {
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
  const newTop = row * rowHeight; //해당 해의 top 값이 223씩 증가

  // 새로운 left와 top 값을 설정
  element.style.left = `${newLeft}%`;
  element.style.top = `${newTop}px`;
  i++;
});

//스크롤 할 시 게시물 추가로 보여짐
$(function () {
  $(".board").slice(0, 8).fadeIn(1000).css("display", "inline-block"); // 초기갯수

  $(window).scroll(function () {
    if ($(window).scrollTop() == $(document).height() - $(window).height()) {
      console.log("스크롤바 끝 도달");

      if ($(".board:hidden").length == 0) {
        // 컨텐츠 남아있는지 확인
        // alert("게시물의 끝입니다."); // 컨텐츠 없을시 alert 창 띄우기
      }
      $(".board:hidden")
        .slice(0, 4)
        .fadeIn(1000)
        .css("display", "inline-block"); // 클릭시 more 갯수 지정
    }
  });
  // 클릭시 more
});

//새로고침시 스크롤 위치 초기화
history.scrollRestoration = "manual";
