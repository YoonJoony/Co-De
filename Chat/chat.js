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
const vote_box = document.getElementById("vote_modal_box");

// 투표 시작
function voteOpen() {
  if (!confirm('ㅇㅇㅇ 사용자에 대한 추방투표를 진행하시겠습니까?')) {
    // 취소 선택시
    return false;
  } else {
    // 확인 선택시
    const vote_box = document.getElementById("vote_modal_box");

    if (vote_box.style.display !== "block") {
      vote_box.style.display = "block";
    }
    else {
      vote_box.style.display = "none";
    }

    // 타이머
    const timer = document.getElementById('timer');
    const lines = timer.querySelector('#lines');
    const fins = timer.querySelector('#fins');
    const nums = timer.querySelector('#num-container');
    const control = document.querySelector('.button-container #control');
    const remainTime = document.querySelector('.time-container #remain-time');
    const totalTime = document.querySelector('.time-container #total-time');

    const endTime = 60

    let intervalID = null;
    let progressTimeSec = 0;

    let isPlay = true;

    function paintLines() {
      for (let i = 0; i < 30; i++) {
        const line = document.createElement('div');
        line.classList.add('line');
        line.style.transform = `rotate(${i * 6}deg)`;

        if (i % 5 == 0) {
          line.classList.add('thick')
        }

        lines.append(line);
      }
    }

    function paintNumber() {
      let left = 15;
      let right = 45;

      for (let i = 0; i < 6; i++) {
        const numBox = document.createElement('div');
        numBox.classList.add('num-box');
        numBox.style.transform = `rotate(${i * 30}deg)`;

        const spanLeft = document.createElement('span');
        const spanRight = document.createElement('span');

        const leftText = left - 5 * i;
        spanLeft.textContent = leftText < 0 ? 60 + leftText : leftText;
        spanRight.textContent = right - (5 * i);

        spanLeft.style.transform = `rotate(${-30 * i}deg)`;
        spanRight.style.transform = `rotate(${-30 * i}deg)`;

        numBox.append(spanLeft, spanRight);
        nums.append(numBox);
      }
    }

    function paintRemainTime() {
      for (let min = 0; min < endTime; min++) {
        for (let sec = 0; sec < 60; sec++) {
          const remainFin = document.createElement('div');
          remainFin.classList.add('fin');

          const deg = min * 6 + sec * 0.1;
          remainFin.style.transform = `rotate(${-deg}deg)`

          fins.append(remainFin);
        }
      }
    }

    function tickSec() {
      progressTimeSec++;
      if (progressTimeSec >= endTime * 60) pause();

      const lastFin = fins.lastChild;

      if (lastFin) {
        lastFin.remove();
      }

      renderRemainTime();
    }

    function play() {
      intervalID = setInterval(tickSec, 16.9)
      isPlay = true;
      control.innerHTML = `<i class="fas fa-pause"></i>`;
    }

    function pause() {
      clearInterval(intervalID);
      isPlay = false;
      control.innerHTML = `<i class="fas fa-play"></i>`;
    }

    function onClickControl() {
      if (isPlay) {
        pause();

      } else {
        play();
      }
    }

    function renderRemainTime() {
      const totalSec = endTime * 60 - progressTimeSec;
      const min = Math.floor(totalSec / 60);
      const sec = totalSec % 60;

      remainTime.textContent = `
        ${min < 10 ? `0${min}` : min} : 
        ${sec < 10 ? `0${sec}` : sec}
    `;
    }

    function paintTime() {
      renderRemainTime();
      totalTime.textContent = `(${endTime} : 00)`;
    }

    if (lines) {
      paintLines();
    }

    if (nums) {
      paintNumber();
    }

    if (fins) {
      paintRemainTime();
    }

    if (control) {
      control.addEventListener('click', onClickControl);
    }

    if (remainTime && totalTime) {
      paintTime();
    }

    play();
  }
}

// 찬성
function ok() {
  vote_box.style.display = "none";
  // 투표 집계

}

// 반대
function nop() {
  vote_box.style.display = "none";
  // 투표 집계

}

// ----------------------------------------------

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

var currentTime = function () {
  var date = new Date();
  var hh = date.getHours();
  var mm = date.getMinutes();
  var apm = hh > 12 ? "오후" : "오전";
  var ct = apm + " " + hh + ":" + mm + "";
  return ct;
}