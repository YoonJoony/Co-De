
/* 소켓 js */
'use strict';
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")


var join = document.querySelector('#chat-join');
var mainJoin = document.querySelector('#main-join');
var main = document.querySelector('#main');
var connectingElement = document.querySelector('connecting');

var stompClient = null;
var nickname = null;

// id 파라미터 가져오기
const url = new URL(location.href).searchParams;
const id = url.get('id');



/* 입장 버튼 누르면 입장 페이지 사라지고 채팅방 페이지가 뜬다. */
function connect(event) {

    nickname = document.querySelector("#user-name").textContent;

    //    입장 버튼 클릭 시 입장 페이지 사라지고 채팅방 페이지가 뜬다
    join.style.opacity = '1';
    main.style.opacity = '1';
    join.style.transform = 'translateY(-900px)';
    setTimeout(function () {
        main.classList.add('visible');
        join.classList.remove('chat-join');
        join.classList.add('hidden');
    }, 550);

    //연결하고자 하는 socket의 endpoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket); //SockJS 객체 기반의 Stomp 클라이언트 객체 생성
    stompClient.connect({}, onConnected, onError); // stomp객체를 활용해 연결 시도
    // connect( {빈 헤더 객체}, 연결 성공 시 실행, 연결 실패시 실행)
    //기본 이벤트 실행 : 입장 버튼 타입 : submit인데 이 메소드로 페이지 이동이 취소됨.
    event.preventDefault();
}

//연결 성공 시
function onConnected() {
    console.log("연결 성공");
    stompClient.subscribe("/sub/mozip/chat/room/" + id, onMessageReceived);
    /*  Stomp 클라이언트 객체를 사용하여 서버로부터 메시지를 구독 한다. 이 메소드는 두 개의 인자를 받는다.
        첫 번째 인자는 구독할 대상의 주소(address)이다. 이 주소는 서버에서 메시지를 보낼 때 사용 된다.
        /sub/chat/room/ 과 roomId를 조합하여 주소를 생성한다. -> 이 주소는 특정 채팅방의 메시지를 구독하는데 사용 됨
        해당 주소를 구독하면 해당 주소의 메시지를 수신할 수 있다는 뜻

        두 번째 인자는 메시지를 수신할 때 호출될 콜백 함수. 이 함수는 onMessagereceived() 와 같이 사용자 정의 함수 or 라이브러리 기본 함수 일 수 있다.
        서버에서 메시지를 수신하면 이 콜백 함수(onMessageReceived) 가 호출된다.
    */

    /* 서버에 username 을 가진 유저가 들어왔다는 것을 알림
       /pub/chat/enterUser 로 메시지를 보냄
       ajax가 라니라 stomp 클라이언트 객체로 보내야 서버에서 stomp 객체를 통해 구독, 메시지 발행 기능을 수행할 수 있음 */
    stompClient.send("/pub/mozip/chat/enterUser",
        {},
        JSON.stringify({
            "id": id,
            sender: nickname,
            type: 'ENTER'
        })
    )
    /*  stompClient.send() 메서드는 Stomp 클라이언트 객체를 사용하여 서버로 메시지를 전송한다.
        이 때 메서드는 세 개의 인자를 받는다
        첫 번째 인자는 메시지를 전송할 대상의 주소. 이 주소는 서버에서 메시지를 받을 때 사용된다.
        여기서는 /pub/chat/enterUser 를 사용하여 채팅방에 참여한 사용자의 정보를 전송한다.

        두 번째 인자는 전송할 헤더 객체. 전송할 메시지에 추가 정보를 포함 한다.

        세 번째 인자는 전송할 메시지의 본문.
        여기서는 JSON.stringify() 함수를 사용하여 객체를 JSON 문자열로 변환한 뒤, 이를 메시지 본문으로 사용한다.
    */
    //    connectingElement.classList.add('hidden');

}

function onError(error) {
    console.log("실패!!");
}

// 유저 리스트 받기
function getUserList() {
    const $list = $('#list');

    $.ajax({
        type: "GET",
        url: "/mozip/chat/userList",
        data: {
            "id": id
        },
        success: function (data) {
            console.log("데이터 받기 성공 : " + data[0]);
            var users = "";
            for (let i = 0; i < data.length; i++) {
                console.log("data[" + i + "] : " + data[i]);
                users += "<li class='dropdown-item'>" + data[i] + "</li>";
            }
            $list.html(users);
        },
        error: function () {
            console.log("리스트 요청 실패 : ");
        }
    })
}



function onMessageReceived(payload) {
    console.log("onMessage");
    var chat = JSON.parse(payload.body);
    // payload는 클라이언트에서 수신한 메시지를 나타냄.
    // 하지만 클라이언트가 수신한건지 서버로 송신한 메시지인지 모르니 payload.body로 수신한 메시지를 확인하는 로직을 써줌
    var messageElement = document.createElement('li'); //li 타입의 [메시지 요소]를 만든다.

    if (chat.type === 'ENTER') {
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();
    }
}







mainJoin.addEventListener('submit', connect, true); //usernameForm 리스너에 connect 함수 연결




$(function () {
    document.getElementById('back-button').addEventListener('click', function () {
        window.history.back();
    });
    // 프로필 클릭시
    var $profile = $(".header-profile");
    var $layerProfile = $(".layer-header-profile");

    $profile
        .on('mouseenter', function (e) {
            e.preventDefault();
            $layerProfile.css({ left: 'auto' }).fadeIn(100);
        })
        .on('mouseleave', function (e) {
            e.preventDefault();
            $layerProfile.css({ left: 'auto' }).fadeOut(100);
        });
});

// 지도

























/* 지도 JS */
$(function () {

    initMap();
});



function initMap() {
    // 호스트 좌표 변수
    let main_lat, main_lng;
    main_lat = 37.64359950713993;
    main_lng = 127.02755816582702;
    const name = "꾸브라꼬";

    var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
    areaArr.push(
        /*이름*/			/*위도*/					/*경도*/
        { location: name, lat: main_lat, lng: main_lng },  // 호스트 좌표
    );


    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열


    // v3 버전 지도 생성
    var map = new naver.maps.Map('map_v3', {
        center: new naver.maps.LatLng(37.64359950713993, 127.02755816582702),
        zoom: 15,
        scaleControl: false,
        logoControl: false,
        mapDataControl: false,
        zoomControl: false,
        mapTypeControl: false
    });


    let Slat = 0, Slng = 0;

    for (var i = 0; i < areaArr.length; i++) {
        Slat += Number(areaArr[i].lat);
        Slng += Number(areaArr[i].lng);
    }

    // 중간지점 마커
    var middlemarker_option = {
        map: map,
        position: new naver.maps.LatLng(main_lat, main_lng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
            size: new naver.maps.Size(60, 62),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    };

    var infoWindow = new naver.maps.InfoWindow({
        content: '<div style="width:10px;text-align:center;padding:10px;"><b>' + areaArr[0].location
    }); // 클릭했을 때 띄워줄 정보 HTML 작성

    markers.push(middlemarker_option); // 생성한 마커를 배열에 담는다.
    infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.



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


    console.log(markers[0], getClickHandler(0));
    naver.maps.Event.addListener(markers[0], 'click', getClickHandler(0)); // 클릭한 마커 핸들러

}

function map_btn() {
    const map_content = document.getElementById("map");

    // 숨기기 (display: none)
    if (map_content.style.display !== "block") {
        map_content.style.display = "block";
    }
    // 보이기 (display: block)
    else {
        map_content.style.display = "none";
    }
}

$(function () {

    initMap2();
});



function initMap2() {
    // 호스트 좌표 변수
    let main_lat, main_lng;
    main_lat = 37.64359950713993;
    main_lng = 127.02755816582702;
    const name = "꾸브라꼬";

    var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
    areaArr.push(
        /*이름*/			/*위도*/					/*경도*/
        { location: name, lat: main_lat, lng: main_lng },  // 호스트 좌표
    );


    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열


    // v3 버전 지도 생성
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.64359950713993, 127.02755816582702),
        zoom: 15,
        logoControl: false,
        mapDataControl: false,
        scaleControl: false,
        mapTypeControl: false,
    });


    let Slat = 0, Slng = 0;

    for (var i = 0; i < areaArr.length; i++) {
        Slat += Number(areaArr[i].lat);
        Slng += Number(areaArr[i].lng);
    }

    // 중간지점 마커
    var middlemarker_option = {
        map: map,
        position: new naver.maps.LatLng(main_lat, main_lng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
            size: new naver.maps.Size(60, 62),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    };

    var infoWindow = new naver.maps.InfoWindow({
        content: '<div style="width:10px;text-align:center;padding:10px;"><b>' + areaArr[0].location
    }); // 클릭했을 때 띄워줄 정보 HTML 작성

    markers.push(middlemarker_option); // 생성한 마커를 배열에 담는다.
    infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.



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


    console.log(markers[0], getClickHandler(0));
    naver.maps.Event.addListener(markers[0], 'click', getClickHandler(0)); // 클릭한 마커 핸들러

}

// -------------------------------------------------------------------------------------------------------------