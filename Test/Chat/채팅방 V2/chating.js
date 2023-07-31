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


const btnOpen = document.getElementById('map_button');
const btnClose = document.getElementById('map_close');
const map_content = document.querySelector('.map');

// 모달창 띄우기
function mapOpen() {
  map_content.style.display = 'block';
  btnOpen.style.display = 'none';
  btnClose.style.display = 'block';
}

// 모달창 닫기
function mapClose() {
  map_content.style.display = 'none';
  btnOpen.style.display = 'block';
  btnClose.style.display = 'none';
}

// 변수 지정
btnOpen.addEventListener('click', mapOpen);
btnClose.addEventListener('click', mapClose);


$(document).ready(function () {
  $('#chating').load('chating.html', function () {

  });
})

// 채팅 관련 js
$(function () {
  $("input[type='text']").keypress(function (e) {
    if (e.keyCode == 13 && $(this).val().length) {
      var _val = $(this).val();
      var _class = $(this).attr("class");
      $(this).val('');
      var _tar = $(".chat_wrap .inner").append('<div class="item ' + _class + '"><div class="box"><p class="msg">' + _val + '</p><span class="time">' + currentTime() + '</span></div></div>');

      var lastItem = $(".chat_wrap .inner").find(".item:last");
      setTimeout(function () {
        lastItem.addClass("on");
      }, 10);

      var position = lastItem.position().top + $(".chat_wrap .inner").scrollTop();
      console.log(position);

      $(".chat_wrap .inner").stop().animate({ scrollTop: position }, 500);
    }
  });

});


// 참가자 리스트
function ListOpen() {
  const list = document.getElementById("vistor");

  if (list.style.display !== "block") {
    list.style.display = "block";
  }
  else {
    list.style.display = "none";
  }
}

// 사용자 초대

$(function () {
  function invitemodalClose() {
    $("#invite_modal").fadeOut();
  }

  $("#invite").click(function () {
    // 초대 하기

    // 백앤드 화이팅

    invitemodalClose(); // 모달 닫기 함수 호출
  });
  $("#invite_btn").click(function () {
    $("#invite_modal").css('display', 'flex').hide().fadeIn(); // 속성 변경 후 hide로 숨기고 fadeIn으로 효과 나타내기
  });
  $("#close").click(function () {
    invitemodalClose(); // 모달 닫기 함수 호출
  });
});

// 투표



// ----------------------------------------------