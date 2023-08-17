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

// 지도

// 다중 마커 표시

$(function () {

    initMap();

});

function initMap() {
    // 호스트 좌표 변수
    let main_lat, main_lng;
    main_lat = 37.64359950713993;
    main_lng = 127.02755816582702;

    var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
    areaArr.push(
        /*이름*/			/*위도*/					/*경도*/
        { location: '농담곰', lat: main_lat, lng: main_lng },  // 호스트 좌표
        { location: '북극곰', lat: '37.643554984480', lng: '127.027856525115' },  // 참가자1 좌표
        { location: '콜라곰', lat: '37.640668966010516', lng: '127.02720710334465' },  // 참가자2 좌표
        { location: '망그곰', lat: ' 37.64302726452654', lng: '127.02830864442011' },  // 참가자3 좌표
    );

    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열

    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(main_lat, main_lng), //지도 시작 지점 = 호스트 좌표
        zoom: 17
    });

    let Slat = 0, Slng = 0;

    for (var i = 0; i < areaArr.length; i++) {
        Slat += Number(areaArr[i].lat);
        Slng += Number(areaArr[i].lng);
    }

    let middlelat, middlelng;

    middlelat = Slat / areaArr.length;
    middlelng = Slng / areaArr.length;


    for (var i = 0; i < areaArr.length; i++) {
        // 좌표 배열의 길이만큼 for문으로 마커와 정보창 매칭

        var marker = new naver.maps.Marker({
            map: map,
            title: areaArr[i].location, // 사용자 이름 
            position: new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng) // 사용자의 위도 경도 넣기 
        });

        /* 정보창 */
        var infoWindow = new naver.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:10px;"><b>' + areaArr[i].location
        }); // 클릭했을 때 띄워줄 정보 HTML 작성

        markers.push(marker); // 생성한 마커를 배열에 담는다.
        infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.
    }

    // 중간지점 마커
    var middlemarker_option = {
        map: map,
        position: new naver.maps.LatLng(middlelat, middlelng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
            size: new naver.maps.Size(60, 62),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    };

    var middlePoint = new naver.maps.Marker(middlemarker_option);


    function getClickHandler(seq) {

        return function (e) {  // 마커를 클릭하는 부분
            var marker = markers[seq], // 클릭한 마커의 시퀀스로 찾는다.
                infoWindow = infoWindows[seq]; // 클릭한 마커의 시퀀스로 찾는다

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker); // 표출
            }
            map.panTo(e.coord); // 마커 클릭시 부드럽게 이동
        }
    }

    for (var i = 0, ii = markers.length; i < ii; i++) {
        console.log(markers[i], getClickHandler(i));
        naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i)); // 클릭한 마커 핸들러
    }

}

// --------------------------------------------------------------

// 지도 버튼
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
// -----------------------------------------------

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