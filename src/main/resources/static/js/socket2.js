//채팅의 핵심
//처음 웹 통신 시작 시 지정된 엔드포인트로 소켓 통신을 시작하고, 지정된 주소를 지속으로 sub(구독) 하게 된다. 또한 지정한 주소로 pub(발송)하는 역할도 한다.

'use strict';
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
var connectingElement = document.querySelector('connecting');

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
    stompClient = Stomp.over(socket); //SockJS 객체를 기반으로 Stomp 클라이언트 객체 생성
                                      //Stomp 클라이언트는 메시지 기반 프로토콜인 Stomp(스트림 오리엔티드 메시지 프로토컬)을 사용하여
                                      //서버와 통신하는데 사용된다.
                                      //socket 객체가 전달되며 Stomp 클라이언트 객체를 반환한다.
                                      //stompClient 객체를 활용하여 서버와 Stomp 프로토콜을 사용하여 통신할 수 있음

    // stompClient에 연결하고, 연결 성공 및 실패 시 콜백 함수를 전달합니다.
    stompClient.connect({}, onConnected, onError); //connect 메소드는 stomp 객체를 활용하여 서버와 연결 시도.
                                                   // connect( {빈 헤더 객체}, 연결 성공 시 호출될 콜백 함수, 연결 실패시...)

    // 기본 이벤트를 취소합니다.
    event.preventDefault();
}

// 연결이 성공적으로 수립되면 호출되는 함수입니다.
function onConnected() {
    console.log("연결 성공!!");
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);
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
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
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

    connectingElement.classList.add('hidden'); //connecting(연결 상태 설명하는 <div> 클래스) 태그 hidden으로 숨김.
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
            "roomId": roomId,
            "username": username
        },
        success: function (data) {
            console.log("함수 동작 확인 : " + data);

            username = data; //중복 닉네임일 경우 닉네임에서 난수 숫자 붙인 후 리턴
        },
        error: function () {
            console.log("실패 : ");
        }
    })

}

// 유저 리스트 받기
// ajax 로 유저 리스를 받으며 클라이언트가 입장/퇴장 했다는 문구가 나왔을 때마다 실행된다.
function getUserList() {
    const $list = $("#list");

    $.ajax({
        type: "GET",
        url: "/chat/userlist",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            console.log("데이터 받기 성공 : " + data[0]);
            var users = "";
            for (let i = 0; i < data.length; i++) {
                console.log("data[i] : "+data[i]);
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }                       //유저를 <li> 태그에 묶어서 users에 저장.
            $list.html(users);      //list.html 메소드로 html에 users를 추가하여 list(<div>) 태그 안에 users에 저장된 태그 추가.
        },
        error: function () {
            console.log("실패 : ");
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
    /*  payload는 WebSocket 클라이언트에서 수신한 메시지를 나타낸다.
        하지만 클라이언트가 수신한건지 서버로 송신한 메시지인지는 모른다.
        따라서 위처럼 payload.body로 수신한 메시지를 확인하는 로직을 써준다.
        JSON.parse(payload.body) 코드로 수신된 JSON 메시지를 자바스크립트 객체로 변환한다.
    */
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

    var contentElement = document.createElement('p');

    // 만약 s3DataUrl 의 값이 null 이 아니라면 => chat 내용이 파일 업로드와 관련된 내용이라면
    // img 를 채팅에 보여주는 작업
    if(chat.s3DataUrl != null){
        var imgElement = document.createElement('img');
        imgElement.setAttribute("src", chat.s3DataUrl);
        imgElement.setAttribute("width", "300");
        imgElement.setAttribute("height", "300");

        var downBtnElement = document.createElement('button');
        downBtnElement.setAttribute("class", "btn fa fa-download");
        downBtnElement.setAttribute("id", "downBtn");
        downBtnElement.setAttribute("name", chat.fileName);
        downBtnElement.setAttribute("onclick", `downloadFile('${chat.fileName}', '${chat.fileDir}')`);


        contentElement.appendChild(imgElement);
        contentElement.appendChild(downBtnElement);

    }else{
        // 만약 s3DataUrl 의 값이 null 이라면 (일반 채팅일 경우)
        // 이전에 넘어온 채팅 내용 보여주기기
       var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);
    }

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
//
//    var textElement = document.createElement('p')// [메시지를 담는 요소] 생성
//    var messageText = document.createTextNode(chat.message);
//    textElement.appendChild(messageText);
//
//    messageElement.appendChild(textElement);
//
//    messageArea.appendChild(messageElement); //메시지 요소를 채팅 메시지 영역에 추가
//    messageArea.scrollTop = messageArea.scrollHeight; //스크롤을 가장 아래로 이동
//    //scrollTop 은 scrollHeight의 전체 높이로 변경 됨 ===> scrollTop(스크롤 바) 가 가장 아래(전체 높이 = 맨 아래) 로 이동됨.
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


/// 파일 업로드 부분 ////
function uploadFile(input){
    if (input.files && input.files[0]) {
        var file = $("#file")[0].files[0]; //선택한 파일 변수에 저장
        var formData = new FormData();
        formData.append("file",file);
        formData.append("roomId", roomId);

        // ajax 로 multipart/form-data 를 넘겨줄 때는
        //         processData: false,
        //         contentType: false
        // 처럼 설정해주어야 한다.

        // 동작 순서
        // post 로 rest 요청한다.
        // 1. 먼저 upload 로 파일 업로드를 요청한다.
        // 2. upload 가 성공적으로 완료되면 data 에 upload 객체를 받고,
        // 이를 이용해 chatMessage 를 작성한다.
        $.ajax({
            type : 'POST',
            url : '/s3/upload',
            data : formData,
            processData: false,
            contentType: false
        }).done(function (data){
            // console.log("업로드 성공")

            var chatMessage = {
                "roomId": roomId,
                sender: username,
                message: username+"님의 파일 업로드",
                type: 'TALK',
                s3DataUrl : data.s3DataUrl, // Dataurl
                "fileName": file.name, // 원본 파일 이름
                "fileDir": data.fileDir // 업로드 된 위치
            };

            // 해당 내용을 발신한다.
            stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        }).fail(function (){
        })
    }
}

// 파일 다운로드 부분 //
// 버튼을 누르면 downloadFile 메서드가 실행됨
// 다운로드 url 은 /s3/download+원본파일이름
function downloadFile(name, dir){
    // console.log("파일 이름 : "+name);
    // console.log("파일 경로 : " + dir);
    let url = "/s3/download/"+name;

    // get 으로 rest 요청한다.
    $.ajax({
        url: "/s3/download/"+name, // 요청 url 은 download/{name}
        data: {
            "fileDir" : dir // 파일의 경로를 파라미터로 넣는다.
        },
        dataType: 'binary', // 파일 다운로드를 위해서는 binary 타입으로 받아야한다.
        xhrFields: {
            'responseType': 'blob' // 여기도 마찬가지
        },
        success: function(data) {

            var link = document.createElement('a');
            link.href = URL.createObjectURL(data);
            link.download = name;
            link.click();
        }
    });
}