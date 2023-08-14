// HTML 요소를 참조하는 변수들을 선언합니다.
var usernamePage = document.querySelector('#username-page'); // 사용자 이름 입력 페이지
var chatPage = document.querySelector('#chat-page'); // 채팅 페이지
var usernameForm = document.querySelector('#usernameForm'); // 사용자 이름 입력 폼
var messageForm = document.querySelector('#messageForm'); // 메시지 입력 폼
var messageInput = document.querySelector('#message'); // 메시지 입력 필드
var messageArea = document.querySelector('#messageArea'); // 채팅 메시지 영역
var connectingElement = document.querySelector('.connecting'); // 연결 상태를 나타내는 요소

// stompClient 및 username 변수를 초기화합니다.
var stompClient = null;
var username = null;

// 채팅 메시지의 아바타 색상 배열을 정의합니다.
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// URL에서 roomId 파라미터를 가져옵니다.
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

// 사용자가 연결되면 호출되는 함수입니다.
function connect(event) {
    // 입력된 사용자 이름을 가져옵니다.
    username = document.querySelector('#name').value.trim();

    // 사용자 이름 중복 확인
    isDuplicateName();

    // usernamePage를 숨기고 chatPage를 표시합니다.
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    // WebSocket 연결을 설정합니다.
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    // stompClient에 연결하고, 연결 성공 및 실패 시 콜백 함수를 전달합니다.
    stompClient.connect({}, onConnected, onError);

    // 기본 이벤트를 취소합니다.
    event.preventDefault();
}

// 연결이 성공적으로 수립되면 호출되는 함수입니다.
function onConnected() {
    // roomId를 사용하여 채팅방에 구독합니다.
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 서버에 사용자가 입장했다는 것을 알립니다.
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
            type: 'ENTER'
        })
    )

    // 연결 중 표시를 숨깁니다.
    connectingElement.classList.add('hidden');
}

// 사용자 이름 중복 확인 함수입니다.
function isDuplicateName() {
    // 서버에 사용자 이름 중복 확인 요청을 보냅니다.
    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "username": username,
            "roomId": roomId
        },
        success: function (data) {
            // 중복된 이름이 없으면 사용자 이름을 설정합니다.
            username = data;
        }
    })
}

// 사용자 목록을 가져오는 함수입니다.
function getUserList() {
    const $list = $("#list");

    // 서버에 사용자 목록 요청을 보냅니다.
    $.ajax({
        type: "GET",
        url: "/chat/userlist",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            // 사용자 목록을 HTML로 구성하여 삽입합니다.
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
        }
    })
}

// 연결에 실패하면 호출되는 함수입니다.
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지를 전송하는 함수입니다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    // 메시지가 있고 stompClient가 연결되어 있으면 메시지를 전송합니다.
    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// 메시지를 받으면 호출되는 함수입니다.
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    // 메시지 유형에 따라 메시지 요소를 구성합니다.
    if (chat.type === 'ENTER') {
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();
    } else if (chat.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();
    } else {
        messageElement.classList.add('chat-message');

        // 아바타 요소를 생성합니다.
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        // 사용자 이름 요소를 생성합니다.
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    // 메시지 텍스트 요소를 생성합니다.
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    // 메시지를 메시지 영역에 추가하고 스크롤을 맨 아래로 이동합니다.
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

// 사용자의 아바타 색상을 가져오는 함수입니다.
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// 이벤트 리스너를 추가합니다.
usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
