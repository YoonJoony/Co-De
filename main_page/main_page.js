// 주소 설정 완료 버튼 클릭시
function Address_commit() { }

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
function myPage() { }

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

let el = document.querySelectorAll(".title");
el.forEach((target) => target.addEventListener("click", show));

function show() {
  document.querySelector(".modal-background").className =
    "modal-background show";
}

function close() {
  document.querySelector(".modal-background").className = "modal-background";
}

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
    $("#modal").css('display', 'flex').hide().fadeIn(); // 속성 변경 후 hide로 숨기고 fadeIn으로 효과 나타내기
  });
  $("#close").click(function () {
    modalClose(); // 모달 닫기 함수 호출
  });
});

// selectbox js

/* ===== Logic for creating fake Select Boxes ===== */
$('.sel').each(function () {
  $(this).children('select').css('display', 'none');

  var $current = $(this);

  $(this).find('option').each(function (i) {
    if (i == 0) {
      $current.prepend($('<div>', {
        class: $current.attr('class').replace(/sel/g, 'sel__box')
      }));

      var placeholder = $(this).text();
      $current.prepend($('<span>', {
        class: $current.attr('class').replace(/sel/g, 'sel__placeholder'),
        text: placeholder,
        'data-placeholder': placeholder
      }));

      return;
    }

    $current.children('div').append($('<span>', {
      class: $current.attr('class').replace(/sel/g, 'sel__box__options'),
      text: $(this).text()
    }));
  });
});

// Toggling the `.active` state on the `.sel`.
$('.sel').click(function () {
  $(this).toggleClass('active');
});

// Toggling the `.selected` state on the options.
$('.sel__box__options').click(function () {
  var txt = $(this).text();
  var index = $(this).index();

  $(this).siblings('.sel__box__options').removeClass('selected');
  $(this).addClass('selected');

  var $currentSel = $(this).closest('.sel');
  $currentSel.children('.sel__placeholder').text(txt);
  $currentSel.children('select').prop('selectedIndex', index + 1);
});


// ----------------------------------------