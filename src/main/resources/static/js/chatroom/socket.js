//채팅의 핵심
//처음 웹 통신 시작 시 지정된 엔드포인트로 소켓 통신을 시작하고, 지정된 주소를 지속으로 sub(구독) 하게 된다. 또한 지정한 주소로 pub(발송)하는 역할도 한다.

'use strict';

//document.write("<script src="jquery-3.5.1.js'></script>
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message'); //입력한 메시지 가져오기
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

// 채팅 메시지의 아바타 색상 배열을 정의합니다.
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

// 사용자가 연결되면 호출되는 함수입니다.

function connect(event) {
    // 입력된 사용자 이름을 가져옵니다.
    username = document.querySelector('#name').value.trim();

    // username 중복 확인
    isDuplicateName();

    // usernamePage 에 hidden 속성 추가해서 가리고
    // chatPage 를 등장시킴
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    // 연결하고자하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    // stompClient에 연결하고, 연결 성공 및 실패 시 콜백 함수를 전달합니다.
    stompClient.connect({}, onConnected, onError);

    // 기본 이벤트를 취소합니다.
    event.preventDefault();
}

// 연결이 성공적으로 수립되면 호출되는 함수입니다.
function onConnected() {

    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);
    // 서버에 username 을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
            type: 'ENTER'
        })
    )

    connectingElement.classList.add('hidden');

}

// 유저 닉네임 중복 확인
function isDuplicateName() {

/*  main-Page 에서 했던 ajax 와는 달리 GET 방식이다.
    data 안에 username, roomId 두 변수가 있는데
    두 변수들을 해당 url의 Controller 로 보낸 후 data 안에 리턴값을 받아온다. */

    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "username": username,
            "roomId": roomId
        },
        success: function (data) {
            console.log("함수 동작 확인 : " + data);

            username = data;
        }
    })

}

// 유저 리스트 받기
// ajax 로 유저 리스를 받으며 클라이언트가 입장/퇴장 했다는 문구가 나왔을 때마다 실행된다.
function getUserList() {
    const $list = $("#list");

    $.ajax({
        type: "GET",
        url: "/chat/userList",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                //console.log("data[i] : "+data[i]);
                users += "<li class='dropdown-item'>" + data[i] + "</li>" //유저를 <li>형태로 저장 후
            }
            $list.html(users);                                            //list.html 메소드로 모내 html을 바꾼다.
        }
    })
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim(); //입력한 메시지 내용을 가져오고, 공백을 제거

    if (messageContent && stompClient) { //입력한 메시지에 내용이 있을 경우와 Stomp 클라이언트가 연결되어있을 경우 체크
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        }; //해당 정보들을 JSON 형식으로 메시지로 전송

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage)); //send(주소, 헤더정보, 전송할 메시지)
        messageInput.value = ''; //메시지 입력창 초기화
    }
    event.preventDefault(); //이벤트 기본 동작을 막는다.
}

//메시지를 받을 경우(JSON 타입)
//넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);

    var messageElement = document.createElement('li'); //li타입의 [메시지 요소] 를 만든다.

    if (chat.type === 'ENTER') { //chatType 이 enter 라면 아래 내용
        messageElement.classList.add('event-message'); //class="" 의 클래스 이름 생성
        chat.content = chat.sender + chat.message;
        getUserList();
    } else if (chat.type === 'LEAVE') { // 자바스크립트 '===' 와 '=='의 차이는 '==='는 유형 및 타입까지 엄격한 비교를 요구함.
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();
    } else { // chatType 이 talk 라면 아래 내용
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i'); // [아바타 요소] 생성
        var avatarText = document.createTextNode(chat.sender[0]); //TextNode는 자식 요소를 가질 수 없고 부모요소의 자식 요소로만 존재할 수 있다.(텍스트를 가지는 노드)
        avatarElement.appendChild(avatarText);   //appendChile : 매개변수에 해당하는 요소를 avatarElement의 자식 요소에 추가한다.
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span'); // [사용자이름 요소] 생성
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p')// [메시지를 담는 요소] 생성
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement); //메시지 요소를 채팅 메시지 영역에 추가
    messageArea.scrollTop = messageArea.scrollHeight; //스크롤을 가장 아래로 이동
    //scrollTop 은 scrollHeight의 전체 높이로 변경 됨 ===> scrollTop(스크롤 바) 가 가장 아래(전체 높이 = 맨 아래) 로 이동됨.
}

//사용자 아바타 색상을 가져오는 함수
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true); //usernameForm 리스너에 connect 함수 연결
messageForm.addEventListener('submit', sendMessage, true); //messageForm 리스너에 sendMessage 함수 연결
