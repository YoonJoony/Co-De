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

    //계좌 저장 시
    $(document).on("click", "#ac_modify_submit", function(e) {
        var modified_ac_user = $("#modified_ac_user");
        var modified_ac_number = $("#modified_ac_number");
        var modified_ac_pw = $("#modified_ac_pw");

        var selectedBank = $("#bank").val();

        if (!modified_ac_user.val() || !modified_ac_number.val() || !modified_ac_pw.val() || !selectedBank) {
            alert("모든 필드를 채워주세요.");
            return; // 경고 메시지 후, 함수 실행 중지
        }
        $.ajax({
            type : "POST",
            url : "/myPage/account/save",
            data : {
                "username" : modified_ac_user.val(),
                "number" : modified_ac_number.val(),
                "password" : modified_ac_pw.val(),
                "accountName" : selectedBank,
            },
            success : function() {
                console.log("계좌 저장에 성공 하였습니다.");
            },
            error : function() {
                console.log("계좌 저장에 실패.");
            }
        })
    });

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

// board 영역 설정
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

//새로고침시 스크롤 위치 초기화
history.scrollRestoration = "manual";

//금액에 콤마(,) 표시
function comma(str) {
  str = String(str);
  return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
}

function uncomma(str) {
  str = String(str);
  return str.replace(/[^\d]+/g, "");
}

function inputNumberFormat(obj) {
  obj.value = comma(uncomma(obj.value));
}

function inputOnlyNumberFormat(obj) {
  obj.value = onlynumber(uncomma(obj.value));
}

function onlynumber(str) {
  str = String(str);
  return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1");
}
// #a_balance 적용
var balance = $("#a_balance").text();
var balance2 = balance.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
$("#a_balance").text(balance2 + "원");

//계좌 저장 요청
