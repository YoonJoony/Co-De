
/* 소켓 js */
'use strict';
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")


var join = document.querySelector('#chat-join');
var mainJoin = document.querySelector('#main-join');
var main = document.querySelector('#main');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message'); //입력한 메시지 가져오기
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var body = document.querySelector('body');

var stompClient = null;
var nickname = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

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
        body.classList.add('body-chat');
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

    /* 서버에 nickname 을 가진 유저가 들어왔다는 것을 알림
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
    connectingElement.innerText = 'Online';
    connectingElement.style.color = '#32e12f';
}

function onError(error) {
    console.log("실패!!");
    connectingElement.innerText = 'Offline';
    connectingElement.style.color = '#ff4a4a';
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

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "id": id,
            sender: nickname,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/mozip/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
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

        var contentElement = document.createElement('p');

        var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);

        messageElement.appendChild(contentElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    } else if (chat.type === 'LEAVE') {
        massageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();

        var contentElement = document.createElement('p');

        var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);

        messageElement.appendChild(contentElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i'); //[아바타 요소 생성]
        var avatarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        var cloudElement = document.createElement('div');
        cloudElement.classList.add('cloud');

        var contentElement = document.createElement('p');
        var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);
        cloudElement.appendChild(contentElement);

        messageElement.appendChild(cloudElement);


        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function uploadFile(input) {

}





mainJoin.addEventListener('submit', connect, true); //usernameForm 리스너에 connect 함수 연결
messageForm.addEventListener('submit', sendMessage, true); //messageForm 리스너에 sendMessage 함수 연결

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
        mapTypeControl: true // 일반, 위성 버튼 보이기 (v3 에서 바뀐 방식)
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
const elements = document.querySelectorAll('.title-proflie');
const header_title = document.querySelector('.chat-header-center');
var i = 0;
elements.forEach((element, index) => {
    const row = Math.floor(index / 4); //현재 요소가 속한 행 번호

    var left = parseFloat(element.style.left); //left값을 받아온다.
    var profile_space = parseFloat(element.style.width);

    if (i % 4 == 0) {
        i = 0;
    }

    const newLeft = i * 30;
    const title_move = profile_space * (i + 1)

    element.style.left = `${newLeft}%`;
    header_title.style.left = `${newLeft - 25}px`;
    i++;
});

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

function list_open() {
    const list_content = document.querySelector(".user-list");

    // 숨기기 (display: none)
    if (list_content.style.display !== "block") {
        list_content.style.display = "block";
    }
    // 보이기 (display: block)
    else {
        list_content.style.display = "none";
    }
}


// // 사용자 추방

// const host = $('#host').text(); // 호스트 닉네임
// const me = $('#me').text(); // 본인 닉네임

// function getOut() {
//     $.ajax({
//         type: "POST", // Post가 리소스 업데이트 할때 쓰는거라고 하던데
//         url: "", // 경로는 제가 지정 할 수는 없으니께
//         data: {
//             "id": id // 추방 할 사람 닉네임 div id
//         },
//         success: function (id) {
//             const outSector = id.parentNode; // 닉네임의 부모노드 검색
//             outSector.remove(); // 해당 닉네임의 리스트 째로 삭제

//             // 실제로 해당 사용자를 채팅방 서버에서 퇴출시키는 동작


//         },
//         error: function () {
//             console.log("요청 실패 : ");
//         }
//     })
// }

const out_button = document.querySelectorAll('.vote');

if (host == me) { // 접속자가 호스트면 추방 js가 활성화
    out_button.classList.add('vote');
    out_button.disabled = false; // 버튼 활성화
    getOut();
    getOut();
} else if (host != me) { // 호스트가 아니면 버튼 비활성화
    out_button.forEach((out_button) => {
        out_button.classList.remove('vote'); // 클래스 삭제
        out_button.disabled = true; // 버튼 비활성화
    });
}

function out_open() {
    const out_content = document.querySelector(".getout");

    // 숨기기 (display: none)
    if (out_content.style.display !== "block") {
        out_content.style.display = "block";
    }
    // 보이기 (display: block)
    else {
        out_content.style.display = "none";
    }

    $.ajax({ // 추방버튼 활성화, 방장 기능 확인
        type: "GET",
        url: "/mozip/chat/findHost",
        data: {
            "id": id,
            'nickname': nickname
        },
        success: function (data) {
            if (data) { //true = 방장
                out_button.classList.add('vote');
                out_button.disabled = false; // 버튼 활성화
                getOut();
            } else {
                out_button.forEach((out_button) => {
                    out_button.classList.remove('vote'); // 클래스 삭제
                    out_button.disabled = true; // 버튼 비활성화
                });
            }
        },
        error: function () {
            alter('추방버튼 활성화 실패');
        }
    })
}

function basket() {
    const basket_content = document.querySelector(".basket-view");

    // 숨기기 (display: none)
    if (basket_content.style.display !== "block") {
        basket_content.style.display = "block";
    }
    // 보이기 (display: block)
    else {
        basket_content.style.display = "none";
    }
}

// 모달창 보여주는 함수
function show_meun() {
    document.querySelector(".modal-background").className = "modal-background show-modal";
}
// 메뉴 수정 버튼 클릭 시 show_modal함수 호출
document
    .querySelector("#modify_menu_btn")
    .addEventListener("click", show_meun);

// 모달 창 닫기
function close_modal() {
    document.querySelector(".modal-background").className = "modal-background";
    detail_close();
}
// x클릭 시 close_modal 함수 호출
document
    .querySelector(".modal-popup-close")
    .addEventListener("click", close_modal);


const detail_content = document.querySelector(".detail_meun_wrap");
function detail_close() {
    detail_content.style.display = "none";
}

function detail_open() {
    detail_content.style.display = "block";
}

// 메뉴상세 수량변경
var count = 1;
var countV = document.querySelector("quantity");
var total_count = document.querySelector("total_price");
var total_count_view = document.querySelector("total_price_view");

function plus() {
    count++;
    countV.value = count;
    total_count_view.value = total_count.value * countV.value;
}

function minus() {
    if (count > 1) {
        count--;
        countV.value = count;
        total_count_view.value = total_count_view.value - total_count.value;
    }
}

var count = 1;
var countEl = document.getElementById("count");
var total_count = document.getElementById("total_count"); //추가
var total_count_view = document.getElementById("total_count_view"); //추가
function plus() {
    count++;
    countEl.value = count;
    total_count_view.value = total_count.value * countEl.value; //추가
}
function minus() {

    if (count > 1) {
        count--;
        countEl.value = count;
        total_count_view.value = total_count_view.value - total_count.value; //추가  
    }
}

// 정산(결제)
// function cal() {
//     const cal_content = document.querySelector(".cal_page");

//     if (cal_content.style.display != "block") {
//         cal_content.style.display = "block";
//     }
//     else {
//         cal_content.style.display = "none";
//     }
// }

function calShow() {
    document.querySelector(".cal_page").className = "cal_page cal_page_show";
}

function calclose() {
    document.querySelector(".cal_page").className = "cal_page";
}

// 유저 리스트 받기
// function getUserList() {
//     const $list = $('#list');

//     $.ajax({
//         type: "GET",
//         url: "/mozip/chat/userList",
//         data: {
//             "id": id
//         },
//         success: function (data) {
//             console.log("데이터 받기 성공 : " + data[0]);
//             var users = "";
//             for (let i = 0; i < data.length; i++) {
//                 console.log("data[" + i + "] : " + data[i]);
//                 users += "<li class='dropdown-item'>" + data[i] + "</li>";
//             }
//             $list.html(users);
//         },
//         error: function () {
//             console.log("리스트 요청 실패 : ");
//         }
//     })
// }

// 계산용 임시 리스트
// const userList = [
//     { id: 1, name: '농담곰', pay:0},
//     { id: 2, name: '북극곰', pay:0},
//     { id: 3, name: '망그곰', pay:0},
//     { id: 4, name: '콜라곰', pay:0}
// ];

const userList = [1, 2, 3];

// const $list = $('#list'); // 참가자 명단

// 개별로 선택한 음식 가격
const pay_amount = 10000;
for (var j = 1; j <= userList.length; j++) {
    document.getElementById('meun_fee' + j).innerHTML = pay_amount.toLocaleString() + " 원"; // toLocaleString() = 숫자에 콤마 찍어주는 함수
}

$('#pay_done').one('click', function () {
    const delivery_fee = 4000; // 배달비
    const delivery_fee_each = Math.ceil(delivery_fee / userList.length); // 각자 내야 하는 배달비

    const host_pay = document.getElementById("host");

    let rate;

    if (userList.length == 4) { // 참가자가 4인 일 때
        rate = Math.ceil(delivery_fee_each * 0.4);
    }
    else if (userList.length == 3) { // 참가자가 3인 일 때
        rate = Math.ceil(delivery_fee_each * 0.3);
    }
    else if (userList.length == 2) { // 참가자가 2인 일 때
        rate = Math.ceil(delivery_fee_each * 0.2);
    }

    let host_fee = Math.ceil(delivery_fee_each - rate); // 호스트 배달비 할인
    // 모달창에 결과 출력
    document.getElementById("host_discount").innerHTML = "-" + rate.toLocaleString() + " 원";
    document.getElementById("host_delifee").innerHTML = host_fee.toLocaleString() + " 원";

    const costomer_add = Math.ceil(rate / (userList.length - 1));
    const costomer_fee = delivery_fee_each + costomer_add; // 할인 된 금액만큼 나머지 사람들이 납부 

    const total_pay_host = pay_amount + host_fee;
    host_pay.innerHTML += `<p class="pay_result">결제금액:  ${total_pay_host.toLocaleString()}원</p> <p class="arrow-down" id="detail_show"
    onclick="detailShow()"></p>`; // 호스트가 내야 하는 비용
    // 모달창에 결과 출력
    document.getElementById("host_totalfee").innerHTML = total_pay_host.toLocaleString() + " 원";

    const total_pay_coutomer = pay_amount + costomer_fee;

    for (var i = 1; i <= (userList.length - 1); i++) {
        var costomer_pay = document.querySelector('p[name=costomer' + i + ']');
        if (i == 1) {
            costomer_pay.innerHTML += `<p class="pay_result">결제금액:  ${total_pay_coutomer.toLocaleString()}원</p><p class="arrow-down "
        onclick="detailShow_cos1()"></p>`; // 참가자가 지불해야 하는 비용
        } else if (i == 2) {
            costomer_pay.innerHTML += `<p class="pay_result">결제금액:  ${total_pay_coutomer.toLocaleString()}원</p><p class="arrow-down "
        onclick="detailShow_cos2()"></p>`; // 참가자가 지불해야 하는 비용
        } else if (i == 3) {
            costomer_pay.innerHTML += `<p class="pay_result">결제금액:  ${total_pay_coutomer.toLocaleString()}원</p><p class="arrow-down "
            onclick="detailShow_cos3()"></p>`; // 참가자가 지불해야 하는 비용
        }

        // 모두 공통인 부분 배열로 인원수 대로 일괄출력
        for (var j = 0; j <= i; j++) {
            document.getElementsByClassName("fee")[j].innerHTML = delivery_fee.toLocaleString() + " 원"; // 원래 배달비
            document.getElementsByName("each_delifee")[j].innerHTML = Math.ceil(delivery_fee_each).toLocaleString() + " 원"; // 상세창에 개별 배달비 출력

            document.getElementsByName("host_discount_add")[j].innerHTML = "+" + costomer_add.toLocaleString() + " 원"; // 참가자 배달비 가액
            document.getElementsByName("costomer_delifee")[j].innerHTML = costomer_fee.toLocaleString() + " 원"; // 참가자 총 배달비

            document.getElementsByName("comtomer_totalfee")[j].innerHTML = total_pay_coutomer.toLocaleString() + " 원"; // 참가자 결제 금액
        }
    }
});
// ------------------------------------------------------------------------------------------

function show() {
    document.querySelector(".header2").className = "header2 header2_show";
}

function close() {
    document.querySelector(".header2").className = "header2";
}

document.querySelector("#chat_show").addEventListener('click', show);
document.querySelector("#chat-page").addEventListener('click', close);



// 호스트 상세 결제창 출력 + 애니메이션 효과
function detailShow() {
    document.querySelector("#pay_detail_host").className = "pay_detail_host pay_detail_show";
}

function detailClose() {
    document.querySelector("#pay_detail_host").className = "pay_detail_host";
}

function detailShow_cos1() {
    document.querySelector("#pay_detail1").className = "pay_detail1 pay_detail_show";
}
function detailClose_cos1() {
    document.querySelector("#pay_detail1").className = "pay_detail1";
}

function detailShow_cos2() {
    document.querySelector("#pay_detail2").className = "pay_detail2 pay_detail_show";
}
function detailClose_cos2() {
    document.querySelector("#pay_detail2").className = "pay_detail2";
}

function detailShow_cos3() {
    document.querySelector("#pay_detail3").className = "pay_detail3 pay_detail_show";
}
function detailClose_cos3() {
    document.querySelector("#pay_detail3").className = "pay_detail3";
}

// 결제창 이동 

const basket_wrap = document.querySelector(".basket-view");
const basket_header = document.querySelector(".basket-title");

let BlastX = 0;
let BlastY = 0;
let BstartX = 0;
let BstartY = 0;

// 장바구니 이동
basket_header.addEventListener('mousedown', function (e) {
    e.preventDefault();
    BstartX = e.clientX;
    BstartY = e.clientY;

    // 2.  
    basket_header.classList.add('active');

    // 3.
    document.addEventListener('mouseup', onRemoveEvent);

    // 4.
    document.addEventListener('mousemove', onMove);
});

function onRemoveEvent() {
    basket_header.classList.remove('active');
    document.removeEventListener('mouseup', onRemoveEvent);
    document.removeEventListener('mousemove', onMove);
}

function onMove(e) {
    e.preventDefault();
    BlastX = BstartX - e.clientX;
    BlastY = BstartY - e.clientY;

    BstartX = e.clientX;
    BstartY = e.clientY;

    basket_wrap.style.top = `${basket_wrap.offsetTop - BlastY}px`;
    basket_wrap.style.left = `${basket_wrap.offsetLeft - BlastX}px`;
}

// 지도 추가

// ---------------------------------------------------------------------------------------------

function map_btn() {
    const map_content = document.getElementById("map");
    const map_title = document.querySelector(".map_title")

    // 숨기기 (display: none)
    if (map_content.style.display !== "block") {
        map_content.style.display = "block";
        map_title.style.display = "block";
    }
    // 보이기 (display: block)
    else {
        map_content.style.display = "none";
        map_title.style.display = "none";
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