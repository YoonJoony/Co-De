//모바일 햄버거 창 클릭 시 보이고 사라지게
function show() {
  document.querySelector(".header2").className = "header2 header2_show";
}

function close() {
  document.querySelector(".header2").className = "header2";
}

document.querySelector("#chat_show").addEventListener("click", show);
document.querySelector("#chat-page").addEventListener("click", close);

/* 소켓 js */
"use strict";
document.write(
  "<script\n" +
    '  src="https://code.jquery.com/jquery-3.6.1.min.js"\n' +
    '  integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ="\n' +
    '  crossorigin="anonymous"></script>'
);

var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message"); //입력한 메시지 가져오기
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");
var body = document.querySelector("body");
var time;
var userList = document.querySelector(".user-list");

var stompClient = null;
var nickname = null;
//var profileImage = null; //자기 프사

var colors = [
  "#2196F3",
  "#32c787",
  "#00BCD4",
  "#ff5652",
  "#ffc107",
  "#ff85af",
  "#FF9800",
  "#39bbb0",
];

// id 파라미터 가져오기
var url;
var id;
var mozipStatus = "정산전"; //정산 상태

/* 입장 버튼 누르면 입장 페이지 사라지고 채팅방 페이지가 뜬다. */
function connect(event) {
  nickname = document.querySelector("#user-name").textContent;
  url = new URL(location.href).searchParams;
  id = url.get("id");

  $.ajax({
    type: "GET",
    url: "/mozip/chat/inquiry",
    data: {
      id: id,
    },
    success: function (data) {
      if (data) {
        mozipStatus = "정산시작"; //정산 시작된 상태일 경우 반환
        console.log(mozipStatus);
      } else {
        mozipStatus = "정산전"; //정산 시작 전일 경우 반환
        console.log(mozipStatus);
      }
      calculateStatus();
    },
    error: function () {},
  });

  //var profileImage = findProfileImage(); 자기 프로필 이미지 경로 찾기

  //연결하고자 하는 socket의 endpoint
  var socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket); //SockJS 객체 기반의 Stomp 클라이언트 객체 생성
  stompClient.connect({}, onConnected, onError); // stomp객체를 활용해 연결 시도
  // connect( {빈 헤더 객체}, 연결 성공 시 실행, 연결 실패시 실행)
  //기본 이벤트 실행 : 입장 버튼 타입 : submit인데 이 메소드로 페이지 이동이 취소됨.
  event.preventDefault();
}

//연결 성공 시
function onConnected() {
  console.log("연결 성공");
  menuList();
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
  stompClient.send(
    "/pub/mozip/chat/enterUser",
    {},
    JSON.stringify({
      id: id,
      sender: nickname,
      type: "ENTER",
      createdAt: new Date(),
    })
  );
  /*  stompClient.send() 메서드는 Stomp 클라이언트 객체를 사용하여 서버로 메시지를 전송한다.
           이 때 메서드는 세 개의 인자를 받는다
           첫 번째 인자는 메시지를 전송할 대상의 주소. 이 주소는 서버에서 메시지를 받을 때 사용된다.
           여기서는 /pub/chat/enterUser 를 사용하여 채팅방에 참여한 사용자의 정보를 전송한다.

           두 번째 인자는 전송할 헤더 객체. 전송할 메시지에 추가 정보를 포함 한다.

           세 번째 인자는 전송할 메시지의 본문.
           여기서는 JSON.stringify() 함수를 사용하여 객체를 JSON 문자열로 변환한 뒤, 이를 메시지 본문으로 사용한다.
       */
  connectingElement.innerText = "Online";
  connectingElement.style.color = "#32e12f";
}

function onError(error) {
  console.log("실패!!");
  connectingElement.innerText = "Offline";
  connectingElement.style.color = "#ff4a4a";
}

// 유저 리스트 받기
function getUserList() {
  const imagePaths = [
    "/images/profile/profile2.png",
    "/images/profile/profile3.png",
    "/images/profile/profile4.png",
    "/images/profile/profile5.png",
  ];

  $.ajax({
    type: "GET",
    url: "/mozip/chat/userList",
    data: {
      id: id,
    },
    success: function (data) {
      const $user_list = $("#user-list");
      var inviteTag = "";
      console.log("데이터 받기 성공 : " + data[0]);

      while (userList.firstChild) {
        userList.removeChild(userList.firstChild);
      }
      var userListContent = document.createElement("div");
      userListContent.classList.add("user-list-content");

      for (let i = 0; i < data.length; i++) {
        var chatUserList = document.createElement("div");
        chatUserList.classList.add("user");

        var chatUserPicture = document.createElement("span");
        chatUserPicture.classList.add("chat-user-picture");
        var chatUserPictureImg = document.createElement("img");
        chatUserPictureImg.src = imagePaths[i];
        chatUserPicture.appendChild(chatUserPictureImg);

        console.log("data[" + i + "] : " + data[i]);

        var chatUser = document.createElement("span");
        chatUser.classList.add("chat-user-name");
        var users = document.createTextNode(data[i]);
        chatUser.appendChild(users);

        //니가 호스트일 경우
        //                if (findHost(id, nickname)){
        //                    chatUser.id = "host";
        //                }

        var exileButton = document.createElement("img");
        exileButton.src = "/images/out.png";
        exileButton.classList.add("exile-button");

        chatUserList.appendChild(chatUserPicture);
        chatUserList.appendChild(chatUser);
        chatUserList.appendChild(exileButton);

        userListContent.appendChild(chatUserList);
      }

      // ****** 중요! 다른사람 들어 올 때마다 프로필 계속 만들어지니 밑에처럼 문자열로 생성해서 html에 붙이지
      inviteTag =
        "<div class='invite' href='#enterRoomModal' data-bs-toggle='modal' data-target='#enterRoomModal'><span class='invite-content'>사용자 초대</span></div>";

      var invite = $(inviteTag)[0]; //위 var inviteTag를 jquery 객체로 변환한다.
      userList.appendChild(userListContent);
      userList.appendChild(invite);

      //밑에 사용자가 호스트일 경우 자기 버튼 지우고 다른 유저 버튼 보이게 하기.
      //사용자가 아닐경우는 버튼이 아얘 안보이게 설정.
    },
    error: function () {
      console.log("리스트 요청 실패 : ");
    },
  });
}

function sendMessage(event) {
  var messageContent = messageInput.value.trim();

  if (messageContent && stompClient) {
    var chatMessage = {
      id: id,
      sender: nickname,
      message: messageInput.value,
      createdAt: new Date(), //채팅친 시간 추가.
      type: "TALK",
    };

    stompClient.send(
      "/pub/mozip/chat/sendMessage",
      {},
      JSON.stringify(chatMessage)
    );
    messageInput.value = "";
  }
  event.preventDefault();
}

//장바구니에 메뉴를 추가했을 경우
function basketSendMessage() {
  if (stompClient) {
    var chatMessage = {
      id: id,
      sender: nickname,
      message: nickname + "님이 장바구니에 메뉴를 추가하였습니다.",
      createdAt: new Date(), //채팅친 시간 추가.
      type: "BASKET",
    };

    stompClient.send(
      "/pub/mozip/chat/sendMessage",
      {},
      JSON.stringify(chatMessage)
    );
  }
}

//장바구니 수량을 변경했을 경우
function basketUpdateSendMessage() {
  if (stompClient) {
    var chatMessage = {
      id: id,
      sender: nickname,
      message: nickname + "님이 메뉴 수량을 변경하셨습니다.",
      createdAt: new Date(), //채팅친 시간 추가.
      type: "UPDATE",
    };

    stompClient.send(
      "/pub/mozip/chat/sendMessage",
      {},
      JSON.stringify(chatMessage)
    );
  }
}

//장바구니 메뉴를 삭제했을 경우
function basketDeleteSendMessage(deleteMenuIdRecv) {
  if (stompClient) {
    var chatMessage = {
      id: id,
      sender: deleteMenuIdRecv,
      message: nickname + "님이 메뉴를 삭제하셨습니다.",
      createdAt: new Date(), //채팅친 시간 추가.
      type: "DELETE",
    };

    stompClient.send(
      "/pub/mozip/chat/sendMessage",
      {},
      JSON.stringify(chatMessage)
    );
  }
}

//정산이 시작되었을 경우
function mozipStatusUpdate(statusUpdateMassage) {
  if (stompClient) {
    var chatMessage = {
      id: id,
      sender: nickname,
      message: nickname + "님이 " + statusUpdateMassage + " 상태로 변경하셨습니다.",
      createdAt: new Date(), //채팅친 시간 추가.
      type: "STATUS",
    };

    stompClient.send(
        "/pub/mozip/chat/sendMessage",
        {},
        JSON.stringify(chatMessage)
    );
  }
}

let lastMessageTimeMinutes = 99;
let lastMessageTimeHour = 99;
let timeDifference = 1;
let lastMessageSender = "";

var deleteMenuId; //삭제한 메뉴 아이디(누가 메뉴를 삭제했을 시 필요)
function onMessageReceived(payload) {
  console.log("onMessage");
  var chat = JSON.parse(payload.body);
  // payload는 클라이언트에서 수신한 메시지를 나타냄.
  // 하지만 클라이언트가 수신한건지 서버로 송신한 메시지인지 모르니 payload.body로 수신한 메시지를 확인하는 로직을 써줌
  var messageElement = document.createElement("li"); //li 타입의 [메시지 요소]를 만든다.

  const currentTime = new Date();

  if (chat.type === "ENTER") {
    messageElement.classList.add("event-message");
    getUserList();

    var contentElement = document.createElement("p");

    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else if (chat.type === "LEAVE") {
    massageElement.classList.add("event-message");
    getUserList();

    var contentElement = document.createElement("p");

    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else if (chat.type === "BASKET") {
    //메뉴 수량 변경시 채팅으로 알림과 동시에 장바구니에 추가
    //다른 사람이 추가한 최신 메뉴를 받음
    $.ajax({
      type: "POST",
      url: "/basket/add/recv",
      data: {
        nickname: chat.sender,
      },
      success: function (data) {
        if (isDuplicateBasket(data) === 1) {
          createBasketMenu(data); //장바구니에 추가한 메뉴 div 생성
        }

        totalPrice();
        console.log("메뉴저장 성공");
      },
      error: function () {
        console.log("리스트 요청 실패 : ");
      },
    });

    messageElement.classList.add("event-message");

    var contentElement = document.createElement("p");

    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else if (chat.type === "UPDATE") {
    //메뉴 수량 변경시 채팅으로 알림
    messageElement.classList.add("event-message");

    var contentElement = document.createElement("p");

    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else if (chat.type === "DELETE") {
    //메뉴 삭제 시 장바구니에서 삭제.
    //chat.sender 안에 닉네임 대신 삭제를 선택한 메뉴의 기본키가 들어있다.
    //삭제는 속도가 빠르게 반영되서 굳이 채팅으로 알리지 않는다
    var element = document.querySelector(
      '[data-room-id="' + chat.sender + '"]'
    );
    if (element) {
      element.remove();
      totalPrice();
    }
  } else if (chat.type === "STATUS") {
    //정산 상태 변경 시 알림
    messageElement.classList.add("event-message");

    var contentElement = document.createElement("p");

    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else {
    messageElement.classList.add("chat-message");

    var avatarElement = document.createElement("i"); //[아바타 요소 생성]
    var avatarText = document.createTextNode(chat.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style["background-color"] = getAvatarColor(chat.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement("span");
    var usernameText = document.createTextNode(chat.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    var chatWrapper = document.createElement("div");
    chatWrapper.classList.add("chat-wrapper");

    var cloudElement = document.createElement("div");
    cloudElement.classList.add("cloud");

    var contentElement = document.createElement("p");
    var messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);
    cloudElement.appendChild(contentElement);

    const hours = currentTime.getHours();
    const minutes = currentTime.getMinutes();
    const timeString = `${hours.toString().padStart(2, "0")}:${minutes
      .toString()
      .padStart(2, "0")}`;

    var createdAt = document.createElement("p");
    createdAt.classList.add("time-text");
    var timeText = document.createTextNode(timeString);
    createdAt.appendChild(timeText);

    const visibleTimeText = document.querySelector(".time-text");
    const bool = 1;

    if (timeDifference < 1) {
      //1분 이내로 채팅 친 경우
      time = document.querySelectorAll(".time-text");
      if (
        lastMessageTimeMinutes == currentTime.getMinutes() &&
        chat.sender == lastMessageSender
      ) {
        if (lastMessageTimeHour == currentTime.getHours()) {
          createdAt.classList.add("hidden");
          usernameElement.classList.add("hidden");
          avatarElement.classList.add("hidden");
          messageElement.classList.remove("chat-message");
          messageElement.classList.add("chat-message-last");
        }
      } else if (
        lastMessageTimeMinutes != currentTime.getMinutes() &&
        chat.sender != lastMessageSender
      ) {
        timeDifference = 1;
      }
    }

    chatWrapper.appendChild(cloudElement);
    chatWrapper.appendChild(createdAt);

    messageElement.appendChild(chatWrapper);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;

    //채팅 같은 사람이 1분 이내로 칠 경우 전에 친 채팅 시간이랑 비교하기 위해 선언
    lastMessageTimeMinutes = currentTime.getMinutes();
    lastMessageTimeHour = currentTime.getHours();
    lastMessageSender = chat.sender;
    timeDifference = 0; //채팅시간차이인데 일단 다른 사람 채팅 치고 내가 처음 채팅칠 경우에만 조건 주기위해 생성함.
    //나중에 진짜 시간 차이를 밀리초로 계산해서 비교해 주는 값을 넣어 줘야함.
  }
  //    timeDifference = Math.abs(currentTime.getTime() - lastMessage.createdAt.getTime())/1000/60;
  //    console.log("현재 시간(밀리초) : " + currentTime.getTime() + " - " + "직전 채팅 시간(밀리초) : " + lastMessage.createdAt.getTime() + " = " + timeDifference);
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
    hash = 31 * hash + messageSender.charCodeAt(i);
  }

  var index = Math.abs(hash % colors.length);
  return colors[index];
}

async function outChat() {
  try {
    const outChatPage = await $.ajax({
      type: "GET",
      url: "/mozip/chat/deleteUser",
      data: {
        "id" : id
      },
    });
    location.href = outChatPage;
  } catch (error) {
    console.log("요청 실패");
  }
}

function uploadFile(input) {}

window.onload = function () {
  connect();
};

messageForm.addEventListener("submit", sendMessage, true); //messageForm 리스너에 sendMessage 함수 연결

$(function () {
  //헤더부분 '내 채팅' 에서 자기 방 들어가게 하기.
  //    $mozipPage.click(function() {
  //      location.href = "/mozip/chat/room?id="+findRoomId(nickname);
  //    });
});

//자기 입장한 방 번호 찾기 함수
//function findRoomId(nickname) {
//    $.ajax({
//        ...
//    })
//  return RoomId;
//}

/* 메뉴 리스트 */
var menu_body = document.querySelector(".menu-body");
var loading_div = document.querySelector(".loading-div");
var min_price = document.querySelector(".min_price");
var delivery_fee = document.querySelector(".delivery_fee");

function menuList() {
  loading_div.style.display = "block";

  $.ajax({
    type: "GET",
    url: "/chat/menuList",
    data: {
      roomId: id,
    },
    success: function (data) {
      min_price.textContent = data.minPrice; //최소 주문금액
      delivery_fee.textContent = data.delivery_fee; //배달비

      for (let i = 0; i < data.menuList_Title.length; i++) {
        //메뉴 그룹(인기메뉴)
        var menu_group = document.createElement("div");
        menu_group.classList.add("menu-group");

        var menu_group_topper = document.createElement("div");
        menu_group_topper.classList.add("menu-group-topper");
        var menu_group_title = document.createElement("div");
        menu_group_title.classList.add("menu-group-title");
        var menu_group_title_text = document.createTextNode(
          data.menuList_Title_Name[i]
        );

        menu_group_title.appendChild(menu_group_title_text);
        menu_group_topper.appendChild(menu_group_title);
        menu_group.appendChild(menu_group_topper);

        //메뉴 타이틀별로(인기메뉴, 등등) 메뉴 리스트가 저장된 리스트 저장.
        var menuList = data.menuList_Title[i];
        for (let j = 0; j < menuList.length; j++) {
          //메뉴가 저장된 리스트 저장.
          var menuItem = menuList[j];

          //메뉴판
          var menu_group_list = document.createElement("div");
          menu_group_list.classList.add("menu-group-list");
          menu_group_list.setAttribute("data-bs-dismiss", "modal");
          //메뉴판 왼쪽(이름, 정보, 가격)
          var group_div_left = document.createElement("div");
          group_div_left.classList.add("group-div-left");
          //메뉴이름
          var menu_name = document.createElement("div");
          menu_name.classList.add("menu-name");
          var menu_name_text = document.createTextNode(menuItem.menuName);
          menu_name.appendChild(menu_name_text);
          //메뉴상세
          var menu_into = document.createElement("div");
          menu_into.classList.add("menu-into");
          var menu_into_text = document.createTextNode(menuItem.menuDesc);
          menu_into.appendChild(menu_into_text);
          //메뉴가격
          var menu_price = document.createElement("div");
          menu_price.classList.add("menu-price");
          var menu_price_text = document.createTextNode(menuItem.menuPrice);
          menu_price.appendChild(menu_price_text);

          group_div_left.appendChild(menu_name);
          group_div_left.appendChild(menu_into);
          group_div_left.appendChild(menu_price);

          //메뉴판 오른쪽(메뉴사진)
          var group_div_right = document.createElement("div");
          group_div_right.classList.add("group-div-right");

          var menu_photo = document.createElement("img");
          menu_photo.setAttribute("src", menuItem.menuPhoto);
          group_div_right.appendChild(menu_photo);

          //메뉴 리스트에 메뉴판 추가
          menu_group_list.appendChild(group_div_left);
          menu_group_list.appendChild(group_div_right);

          //메뉴 그룹에 메뉴 추가
          menu_group.appendChild(menu_group_list);
        }

        menu_body.appendChild(menu_group);
        loading_div.style.display = "none";
      }
      document.getElementById("choice-menu").onclick = null;
    },
    error: function () {
      console.log("리스트 요청 실패 : ");
    },
  });
}

//장바구니 중복
function isDuplicateBasket(data) {
  let foodNameElements = document.querySelectorAll(".food-name");
  let foodDuplicateBool = 0;

  // 각 요소에 대해 처리
  foodNameElements.forEach(function (element) {
    let text = element.textContent;
    // 텍스트가 "스테이크"인 경우 처리
    if (text === data.product_name) {
      //텍스트가 '스테이크'인 요소의 기본키를 갖고온다.
      let basketId = element.parentElement.parentElement.dataset.roomId;
      //같은 음식일 경우와 그 음식을 시킨 닉네임이 다를 경우 제외한다. 같은 음식을 같은 사람이 시킬 경우 개수가 증가하게 해야함.
      if (parseInt(basketId) !== data.id) {
        return 2;
      }

      let parentElement = element.parentElement.parentElement;

      let countElement = parentElement.querySelector(".count_css");
      let priceElement = parentElement.querySelector(".price");

      let count = parseInt(countElement.value);
      let price = parseInt(priceElement.textContent.replace(/\D/g, ""));

      countElement.value = count + 1;
      priceElement.textContent = price + price / count + "원";
      foodDuplicateBool = 1;
      return 0;
    }
  });
  if (foodDuplicateBool === 1) return 0;

  return 1;
}

//징비구니 추가시 div 생성
function createBasketMenu(data) {
  var basketListItem = document.createElement("div");
  basketListItem.className = "basket-list-item";
  basketListItem.setAttribute("data-room-id", data.id);

  var basketListHeader = document.createElement("div");
  basketListHeader.className = "basket-list-header";
  var foodName = document.createElement("div");
  foodName.className = "food-name";
  foodName.innerText = data.product_name;
  var cancel = document.createElement("div");
  cancel.className = "cancel";
  var img = document.createElement("img");
  img.setAttribute("src", "/images/close.png");
  cancel.appendChild(img);
  basketListHeader.appendChild(foodName);
  basketListHeader.appendChild(cancel);

  var foodInfo = document.createElement("div");
  foodInfo.className = "food-info";
  var price = document.createElement("div");
  price.className = "price";
  price.setAttribute("id", "total_count_view");
  price.setAttribute("wfd-id", "id4");
  price.innerText = data.price * data.quantity + "원";
  foodInfo.appendChild(price);

  var foodOrderDiv = document.createElement("div");
  foodOrderDiv.className = "food-order-div";
  var foodOrder = document.createElement("a");
  foodOrder.className = "food-order";
  foodOrder.innerText = "주문자 : ";
  var foodOrderName = document.createElement("div");
  foodOrderName.className = "food-order-name";
  foodOrderName.innerText = data.nickname;

  var foodInfoUpdate = document.createElement("div");
  foodInfoUpdate.className = "food-info-update";

  var minusButton = document.createElement("input");
  minusButton.setAttribute("type", "button");
  minusButton.setAttribute("value", "-");
  minusButton.setAttribute("id", "minus");
  minusButton.setAttribute("class", "update-button");

  var countInput = document.createElement("input");
  countInput.setAttribute("class", "count_css");
  countInput.setAttribute("type", "text");
  countInput.setAttribute("size", "25");
  countInput.setAttribute("value", data.quantity);
  countInput.setAttribute("id", "count");
  countInput.setAttribute("readonly", "");

  var plusButton = document.createElement("input");
  plusButton.setAttribute("type", "button");
  plusButton.setAttribute("value", "+");
  plusButton.setAttribute("id", "plus");
  plusButton.setAttribute("class", "update-button");

  foodInfoUpdate.appendChild(minusButton);
  foodInfoUpdate.appendChild(countInput);
  foodInfoUpdate.appendChild(plusButton);

  foodOrderDiv.appendChild(foodOrder);
  foodOrderDiv.appendChild(foodOrderName);
  foodOrderDiv.appendChild(foodInfoUpdate);

  basketListItem.appendChild(basketListHeader);
  basketListItem.appendChild(foodInfo);
  basketListItem.appendChild(foodOrderDiv);

  document.querySelector(".basket-list").appendChild(basketListItem);
  return 1;
}
//정산 상태 변경 시
var startSettlement;
var cancelSettlement;

function calculateStatus() {
  if (mozipStatus === "정산시작") {
    startSettlement = document.getElementById("start-settlement");
    if (startSettlement != null) {
      startSettlement.value = "정산취소";
      startSettlement.id = "cancel-settlement";
    }
  } else if (mozipStatus === "정산전") {
    cancelSettlement = document.getElementById("cancel-settlement");
    if (cancelSettlement != null) {
      cancelSettlement.value = "정산시작";
      cancelSettlement.id = "start-settlement";
    }
  }
}
//장바구니 닫기 버튼 시 모달 사라짐
const basket_content = document.querySelector(".basket-view");
function basketClose() {
  if (basket_content.style.display !== "block") {
    basket_content.style.display = "block";
  } else {
    basket_content.style.display = "none";
  }
}

//선택한 메뉴 장바구니에 담기
var menuName;
var menuPrice;
var basketList = document.querySelector(".basket-list");
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

  // 채팅방 프로필 클릭시
  var $chatProfile = $(".chat-header-left");
  var $userList = $(".user-list");

  $chatProfile
    .on("mouseenter", function (e) {
      e.preventDefault();
      $userList.css({ left: "auto" }).fadeIn(100);
    })
    .on("mouseleave", function (e) {
      e.preventDefault();
      $userList.css({ left: "auto" }).fadeOut(100);
    });

  //페이지 이동 .. 잡 처리
  var $mozipPage = $(".mozipPage");

  $mozipPage.click(function () {
    location.href = "/main_page.html";
  });
  

  async function myChat() {
    try {
      const response = await $.ajax({
        type: "GET",
        url: "/mozip/chat/myChatroom",
        data: {},
      });
      location.href = "/mozip/chat/room?id=" + response;
    } catch (error) {
      alert("채팅방에 입장해주세요!");
    }
  }

  //메뉴 선택
  $(document).on("click", ".menu-group-list", function () {
    //메뉴판 선택
    menuName = $(this).find(".menu-name").text(); //내가 선택한 메뉴 이름
    menuPrice = $(this).find(".menu-price").text(); //내가 선택한 메뉴 가격
    //메뉴 정보 전송
    $.ajax({
      type: "POST",
      url: "/basket/add",
      data: {
        chatroom_id: id,
        menuName: menuName,
        menuPrice: menuPrice,
      },
      success: function (data) {
        basketSendMessage(); //채팅으로 장바구니에 메뉴를 추가했다고 알림
        console.log("메뉴저장 성공");
      },
      error: function (data) {
        var response = JSON.parse(data.responseText);
        alert(response.message);
      },
    });

    console.log(menuName);
    console.log(menuPrice);
  });

  //장바구니 조회
  $(document).on("click", "#basket", function () {
    calculateStatus(); //정산 상태 조회
    //basket_content = basketClose() 메소드 위에 작성함
    if (basket_content.style.display !== "block") {
      basket_content.style.display = "block";
    } else {
      basket_content.style.display = "none";
    }
    //메뉴판 선택
    $.ajax({
      type: "GET",
      url: "/chat/basket",
      data: {
        roomId: id,
      },
      success: function (data) {
        while (basketList.firstChild) {
          basketList.removeChild(basketList.firstChild);
        }

        for (let i = 0; i < data.length; i++) {
          createBasketMenu(data[i]); //장바구니에 추가한 메뉴 div 생성
        }
        totalPrice();
      },
      error: function () {
        console.log("리스트 요청 실패 : ");
      },
    });
  });

  //장바구니 수량 변경
  var countInput; //장바구니 수량 카운트
  var updateQuantityMenu; //수량 수정할 메뉴 이름
  var updateQuantityPrice; //수량 수정할 메뉴의 가격
  var updateQuantityNickName; //수량 수정할 메뉴의 주문자.
  var menuId; //메뉴판 기본키

  // + 버튼
  $(document).on("click", "#plus", function () {
    countInput = $(this).parent().find(".count_css"); //클릭한 메뉴의 수량
    //장바구니에 추가한 메뉴 요소에 data-room-id 값(메뉴 기본키)을 가져와 메뉴를 구분해줌
    menuId = $(this).parents(".basket-list-item").data("room-id");
    updateQuantityPrice = $(this).parents(".basket-list-item").find(".price");
    updateQuantityNickName = $(this)
      .parents(".basket-list-item")
      .find(".food-order-name")
      .text();

    //다른 사람의 장바구니를 수정하려 했을 경우.
    if (updateQuantityNickName === nickname && mozipStatus === "정산전") {
      updateQuantityPrice.text(
        parseInt(updateQuantityPrice.text()) +
          parseInt(updateQuantityPrice.text()) / parseInt(countInput.val()) +
          "원"
      );
      countInput.val(parseInt(countInput.val()) + 1);
      totalPrice();
    }

    $.ajax({
      type: "POST",
      url: "/chat/basket/plusQuantity",
      data: {
        chatroom_id: id,
        menuId: menuId,
      },
      success: function (data) {
        basketUpdateSendMessage();
        console.log(data);
      },
      error: function (data) {
        var response = JSON.parse(data.responseText);
        alert(response.message);
      },
    });
  });

  // - 버튼
  $(document).on("click", "#minus", function () {
    countInput = $(this).parent().find(".count_css"); //내가 선택한 메뉴 이름
    updateQuantityPrice = $(this).parents(".basket-list-item").find(".price");
    //메뉴판 요소에 추가된 data-room-id 데이터에 메뉴의 기본키를 넣어서 메뉴판을 구분해줌
    menuId = $(this).parents(".basket-list-item").data("room-id");

    //기존 수량이 1이면 감소 불가능.
    if (parseInt(countInput.val()) > 1) {
      updateQuantityNickName = $(this)
        .parents(".basket-list-item")
        .find(".food-order-name")
        .text();

      //다른 사람의 장바구니를 수정하려 했을 경우 방지
      if (updateQuantityNickName === nickname && mozipStatus === "정산전") {
        updateQuantityPrice.text(
          parseInt(updateQuantityPrice.text()) -
            parseInt(updateQuantityPrice.text()) / parseInt(countInput.val()) +
            "원"
        );
        countInput.val(parseInt(countInput.val()) - 1);
        totalPrice();
      }

      $.ajax({
        type: "POST",
        url: "/chat/basket/minusQuantity",
        data: {
          chatroom_id: id,
          menuId: menuId,
        },
        success: function (data) {
          basketUpdateSendMessage();
          console.log(data);
        },
        error: function (data) {
          var response = JSON.parse(data.responseText);
          alert(response.message);
        },
      });
    }
  });

  //장바구니 1개 삭제
  $(document).on("click", ".cancel", function () {
    //장바구니에 추가한 메뉴 요소에 data-room-id 값(메뉴 기본키)을 가져와 메뉴를 구분해줌
    menuId = $(this).parents(".basket-list-item").data("room-id");
    updateQuantityNickName = $(this)
      .parents(".basket-list-item")
      .find(".food-order-name")
      .text();

    //다른 사람의 장바구니를 수정하려 했을 경우 방지
    if (updateQuantityNickName === nickname && mozipStatus === "정산전") {
      //선택한 메뉴 삭제
      basketDeleteSendMessage(menuId);
    }

    $.ajax({
      type: "POST",
      url: "/chat/basket/deleteByMenu",
      data: {
        chatroom_id: id,
        menuId: menuId,
      },
      success: function (data) {
        if (data === "") {
          alert("본인의 메뉴만 삭제할 수 있습니다!!");
          return;
        }
        console.log(data);
      },
      error: function (data) {
        var response = JSON.parse(data.responseText);
        alert(response.message);
      },
    });
  });

  //정산하기 버튼
  $(document).on("click", "#start-settlement", function () {
    //사용자가 호스트인지 아닌지 구분한다.
    $.ajax({
      type: "GET",
      url: "/mozip/chat/startCalculate",
      data: {
        chatroom_id: id,
        nickname: nickname,
      },
      success: function (data) {
        if (data) {
          alert(data);
          mozipStatus = "정산시작";
          mozipStatusUpdate(mozipStatus);
          calculateStatus();
        }
      },
      error: function (data) {
        var response = JSON.parse(data.responseText);
        alert(response.message);
      },
    });
  });

  //정산 취소 버튼
  $(document).on("click", "#cancel-settlement", function () {
    //사용자가 호스트인지 아닌지 구분한다.
    $.ajax({
      type: "GET",
      url: "/mozip/chat/preCalculateStartStatus",
      data: {
        chatroom_id: id,
        nickname: nickname,
      },
      success: function (data) {
        if (data) {
          alert(data);
          mozipStatus = "정산전";
          mozipStatusUpdate(mozipStatus);
          calculateStatus();
        }
      },
      error: function (data) {
        var response = JSON.parse(data.responseText);
        alert(response.message);
      },
    });
  });
  $(document).on("click", "#deleteMozip", function () {
    console.log("error11");
    $.ajax({
      type: "POST",
      url: "/mozip/deleteChatRoom",
      data: {
        "id": id,
      },
      success: function (data) {
        if (data === "error"){
          alert("삭제 중 오류가 발생했습니다.");
        }else{
          window.location.href = "/main_page.html";
        }
      },
      error: function () {
        console.log("통신 실패");
      },
    });
  });
  initMap2();
});

//총 금액 실시간으로 변동 보이게
var totalPriceDiv = $("#totalPrice");
function totalPrice() {
  var totalPriceValues = 0;

  // 클래스 이름이 "price"인 모든 요소 선택
  let priceElements = document.querySelectorAll(".price");

  // 각 요소에 대해 처리
  priceElements.forEach(function (element) {
    let text = element.textContent;

    // 숫자 추출
    totalPriceValues = totalPriceValues + parseInt(text.replace(/\D/g, ""));
    totalPriceDiv.text(totalPriceValues + "원");
  });
}

function totalRealPrice() {
  $.ajax({
    type: "GET",
    url: "/chat/basket/totalPrice",
    data: {
      id: id,
    },
    success: function (data) {
      for (let i = 0; i < data.length; i++) {
        console.log(data[i].username + ": " + data[i].totalPrice);
      }
    },
    error: function () {
      console.log("장바구니 메뉴 삭제 API 오류");
    },
  });
}

//정산 창
// const $list = $('#list'); // 참가자 명단



var calualtor = document.querySelector(".calualtor");
function calShow() {
  // delivery_fee.innerText(배달요금) 숫자만 빼기
  var del_fee_before = delivery_fee.innerText;
  var ex1 = "sada3000sf";
  var regex = /[^0-9]/g;
  var del_fee = del_fee_before.replace(regex, "");
  var userListLength = [];
  var menu_price = [];

  $.ajax({
    type: "GET",
    async: false,
    url: "/chat/basket/totalPrice",
    data: {
      id: id,
    },
    success: function (data) {
      for (let i = 0; i < data.length; i++) {
        // console.log(data[i].username + ": " + data[i].totalPrice);

        userListLength.push(data[i].username);

        menu_price.push(data[i].totalPrice);
        console.log(menu_price);
      }
    },
    error: function () {
      console.log("장바구니 메뉴 불러오기 오류");
    },
  });

  PaymentDetailsLoad(
    userListLength.length - 1,
    userListLength,
    menu_price,
    del_fee
  );

  for (var k = 1; k < userListLength.length; k++) {
    var pay_username = document.createElement("p");
    pay_username.className = "pay_username";
    pay_username.id = "pay_username" + k;
    var pay_username_txt = document.createTextNode(userListLength[k]);
    pay_username.appendChild(pay_username_txt);

    calualtor.appendChild(pay_username);
  }

  for (var k = 1; k < userListLength.length; k++) {
    //이벤트 추가
    switch (k) {
      case 1:
        $("#pay_username1").on("click", detailShow_cos1);
      case 2:
        $("#pay_username2").on("click", detailShow_cos2);
      case 3:
        $("#pay_username3").on("click", detailShow_cos3);
    }
  }
  document.querySelector(".cal_page").className = "cal_page cal_page_show";
}

function calclose() {
  document.querySelector(".cal_page").className = "cal_page";
  while (calualtor.firstChild) {
    calualtor.removeChild(calualtor.firstChild);
  }

  let parent = document.getElementById("pay_detail_div");
  while (parent.children.length > 0) {
    parent.removeChild(parent.lastChild);
  }
}

// 개별로 선택한 음식 가격
const pay_amount = 10000;
for (var j = 1; j <= userListLength.length; j++) {
  document.getElementById("meun_fee" + j).innerHTML =
    pay_amount.toLocaleString() + " 원"; // toLocaleString() = 숫자에 콤마 찍어주는 함수
}

// 호스트 상세 결제창 출력 + 애니메이션 효과
function detailShow() {
  document.querySelector("#pay_detail_host").className =
    "pay_detail_host pay_detail_show";
}

function detailClose() {
  document.querySelector("#pay_detail_host").className = "pay_detail_host";
}

function detailShow_cos1() {
  document.querySelector("#pay_detail1").className =
    "pay_detail1 pay_detail_show";
}
function detailClose_cos1() {
  document.querySelector("#pay_detail1").className = "pay_detail1";
}

function detailShow_cos2() {
  document.querySelector("#pay_detail2").className =
    "pay_detail2 pay_detail_show";
}
function detailClose_cos2() {
  document.querySelector("#pay_detail2").className = "pay_detail2";
}

function detailShow_cos3() {
  document.querySelector("#pay_detail3").className =
    "pay_detail3 pay_detail_show";
}
function detailClose_cos3() {
  document.querySelector("#pay_detail3").className = "pay_detail3";
}

// 장바구니 이동

// 지도 모달
function map_btn() {
  const map_content = document.getElementById("map");
  const map_title = document.querySelector(".map_title");

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

function initMap2() {
  // 호스트 좌표 변수
  let main_lat, main_lng;
  main_lat = 37.64359950713993;
  main_lng = 127.02755816582702;
  const name = "꾸브라꼬";

  var areaArr = new Array(); // 지역을 담는 배열 ( 지역명/위도경도 )
  areaArr.push(
    /*이름*/ /*위도*/ /*경도*/
    { location: name, lat: main_lat, lng: main_lng } // 호스트 좌표
  );

  let markers = new Array(); // 마커 정보를 담는 배열
  let infoWindows = new Array(); // 정보창을 담는 배열

  // v3 버전 지도 생성
  var map = new naver.maps.Map("map", {
    center: new naver.maps.LatLng(37.64359950713993, 127.02755816582702),
    zoom: 15,
    logoControl: false,
    mapDataControl: false,
    scaleControl: false,
    mapTypeControl: false,
  });

  let Slat = 0,
    Slng = 0;

  for (var i = 0; i < areaArr.length; i++) {
    Slat += Number(areaArr[i].lat);
    Slng += Number(areaArr[i].lng);
  }

  // 중간지점 마커
  var middlemarker_option = {
    map: map,
    position: new naver.maps.LatLng(main_lat, main_lng), // 사용자의 위도 경도 넣기
    title: "중간지점",
    icon: {
      url: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png",
      size: new naver.maps.Size(60, 62),
      origin: new naver.maps.Point(0, 0),
      anchor: new naver.maps.Point(25, 26),
    },
  };

  var infoWindow = new naver.maps.InfoWindow({
    content:
      '<div style="width:10px;text-align:center;padding:10px;"><b>' +
      areaArr[0].location,
  }); // 클릭했을 때 띄워줄 정보 HTML 작성

  markers.push(middlemarker_option); // 생성한 마커를 배열에 담는다.
  infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.

  var middlePoint = new naver.maps.Marker(middlemarker_option);

  function getClickHandler(seq) {
    return function (e) {
      // 마커를 클릭하는 부분
      var marker = markers[seq], // 클릭한 마커의 시퀀스로 찾는다.
        infoWindow = infoWindows[seq]; // 클릭한 마커의 시퀀스로 찾는다

      if (infoWindow.getMap()) {
        infoWindow.close();
      } else {
        infoWindow.open(map, marker); // 표출
      }
      map.panTo(e.coord); // 마커 클릭시 부드럽게 이동
    };
  }

  console.log(markers[0], getClickHandler(0));
  naver.maps.Event.addListener(markers[0], "click", getClickHandler(0)); // 클릭한 마커 핸들러
}

// 결제하기 창 -> 금액확인 -> 주문내역 창 생성 js
// 참가자만 주문 내역 추가

// PaymentDetailsLoad(사람수(숫자), 사람리스트(배열), menu_price, del_fee)
function PaymentDetailsLoad(num_people, user_list, menu_price, del_fee) {
  // storeModal_header_topper_back_img_pay_detail (img)
  for (var i = 1; i <= num_people; i++) {
    var storeModal_header_topper_back_img_pay_detail =
      document.createElement("img");
    storeModal_header_topper_back_img_pay_detail.className =
      "storeModal_header_topper_back_img_pay_detail";
    storeModal_header_topper_back_img_pay_detail.id = "shtbipdc" + i;
    storeModal_header_topper_back_img_pay_detail.src = "/images/back.png";

    // (p)
    var p1 = document.createElement("p");
    var p1_text = document.createTextNode(user_list[i]);
    p1.appendChild(p1_text);

    // (li)
    var li1 = document.createElement("li");
    var li1_txt = document.createTextNode(" 주문 내역 ");
    li1.appendChild(li1_txt);

    // detail_hr1 (hr)
    var detail_hr1 = document.createElement("hr");
    detail_hr1.className = "detail_hr";

    // detail_hr2 (hr)
    var detail_hr2 = document.createElement("hr");
    detail_hr2.className = "detail_hr";

    // pay_detail_text (li) 메뉴이름
    var pay_detail_text1 = document.createElement("li");
    pay_detail_text1.className = "pay_detail_text";
    var pay_detail_text1_txt = document.createTextNode("담은 메뉴 가격 합계");
    pay_detail_text1.appendChild(pay_detail_text1_txt);

    // id = meun_fee2 (p)
    var pay_text1 = document.createElement("p");
    pay_text1.className = "pay_text";
    pay_text1.id = "meun_fee2";
    var pay_text1_txt = document.createTextNode(
      menu_price[i].toLocaleString() + "원" // (메뉴합계)
    );
    pay_text1.appendChild(pay_text1_txt);

    // total_pay (div)
    var total_pay1 = document.createElement("div");
    total_pay1.className = "total_pay";
    // 자식 요소 추가
    total_pay1.appendChild(pay_detail_text1);
    total_pay1.appendChild(pay_text1);

    // pay_detail_text (li) 배달요금
    var pay_detail_text2 = document.createElement("li");
    pay_detail_text2.className = "pay_detail_text";
    var pay_detail_text2_txt = document.createTextNode(" 배달요금 ");
    pay_detail_text2.appendChild(pay_detail_text2_txt);

    // id = fee (p)
    var pay_text2 = document.createElement("p");
    pay_text2.className = "pay_text fee";
    pay_text2.id = "fee";
    var pay_text2_txt = document.createTextNode(
      parseInt(del_fee).toLocaleString() + "원" // (배달요금)
    );
    pay_text2.appendChild(pay_text2_txt);

    // delivery_fee_div (div)
    var delivery_fee_div = document.createElement("div");
    delivery_fee_div.className = "delivery_fee_div";
    // 자식 요소 추가
    delivery_fee_div.appendChild(pay_detail_text2);
    delivery_fee_div.appendChild(pay_text2);

    // pay_detail_text (li) 인당요금
    var pay_detail_text3 = document.createElement("li");
    pay_detail_text3.className = "pay_detail_text";
    var pay_detail_text3_txt = document.createTextNode(" 인당요금 ");
    pay_detail_text3.appendChild(pay_detail_text3_txt);

    // name = each_delifee (p)
    var pay_text3 = document.createElement("p");
    pay_text3.className = "pay_text";
    pay_text3.name = "each_delifee";
    var pay_text3_txt = document.createTextNode(
      (parseInt(del_fee) / (num_people + 1)).toLocaleString() + "원"
    ); // (인당요금)
    pay_text3.appendChild(pay_text3_txt);

    // per_fee_div (div)
    var per_fee_div = document.createElement("div");
    per_fee_div.className = "per_fee_div";
    // 자식 요소 추가
    per_fee_div.appendChild(pay_detail_text3);
    per_fee_div.appendChild(pay_text3);

    // pay_detail_text (li) 방장할인 가액
    // var pay_detail_text4 = document.createElement("li");
    // pay_detail_text4.className = "pay_detail_text";
    // var pay_detail_text4_txt = document.createTextNode(" 방장할인 가액 ");
    // pay_detail_text4.appendChild(pay_detail_text4_txt);

    // name = host_discount_add (p)
    // var pay_text4 = document.createElement("p");
    // pay_text4.className = "pay_text discount_plus";
    // pay_text4.name = "host_discount_add";
    // var pay_text4_txt = document.createTextNode("(방장할인 가액)");
    // pay_text4.appendChild(pay_text4_txt);

    // per_fee_div (div)
    // var host_fee_div = document.createElement("div");
    // host_fee_div.className = "host_fee_div";
    // // 자식 요소 추가
    // host_fee_div.appendChild(pay_detail_text4);
    // host_fee_div.appendChild(pay_text4);

    // pay_detail_text (li) 지불 배달요금
    var pay_detail_text5 = document.createElement("li");
    pay_detail_text5.className = "pay_detail_text";
    var pay_detail_text5_txt = document.createTextNode(" 지불 배달요금 ");
    pay_detail_text5.appendChild(pay_detail_text5_txt);

    // name = costomer_delifee (p)
    var pay_text5 = document.createElement("p");
    pay_text5.className = "pay_text";
    pay_text5.name = "costomer_delifee";
    var pay_text5_txt = document.createTextNode(
      (parseInt(del_fee) / (num_people + 1)).toLocaleString() + "원"
    ); // (지불 배달 요금)
    pay_text5.appendChild(pay_text5_txt);

    // total_pay1 (div)
    var total_pay2 = document.createElement("div");
    total_pay2.className = "total_pay";
    // 자식 요소 추가
    total_pay2.appendChild(pay_detail_text5);
    total_pay2.appendChild(pay_text5);

    // pay_detail_text (li) 결제 금액
    var pay_detail_text6 = document.createElement("li");
    pay_detail_text6.className = "pay_detail_text";
    var pay_detail_text6_txt = document.createTextNode(" 결제 금액 ");
    pay_detail_text6.appendChild(pay_detail_text6_txt);

    // name = comtomer_totalfee (p)
    var pay_text6 = document.createElement("p");
    pay_text6.className = "pay_text";
    pay_text6.name = "comtomer_totalfee";
    var pay_text6_txt = document.createTextNode(
      (menu_price[i] + parseInt(del_fee) / (num_people + 1)).toLocaleString() +
        "원"
    ); // (결제 금액)
    pay_text6.appendChild(pay_text6_txt);

    // total_pay2 (div)
    var total_pay3 = document.createElement("div");
    total_pay3.className = "total_pay";
    // 자식 요소 추가
    total_pay3.appendChild(pay_detail_text6);
    total_pay3.appendChild(pay_text6);

    //-----------------------------------------------------------

    var pay_detail = document.createElement("div");
    pay_detail.className = "pay_detail" + i;
    pay_detail.id = "pay_detail" + i;

    // storeModal_header_topper_back_img_pay_detail.onclick = function () {
    //   document.querySelector(".pay_detail1").className = "pay_detail1";
    //   console.log("지랄");
    // };
    // 자식 요소 추가
    pay_detail.appendChild(storeModal_header_topper_back_img_pay_detail);
    pay_detail.appendChild(p1);
    pay_detail.appendChild(li1);
    pay_detail.appendChild(document.createElement("br"));
    pay_detail.appendChild(detail_hr1);
    pay_detail.appendChild(total_pay1);
    pay_detail.appendChild(document.createElement("br"));
    pay_detail.appendChild(delivery_fee_div);
    pay_detail.appendChild(document.createElement("br"));
    pay_detail.appendChild(per_fee_div);
    pay_detail.appendChild(document.createElement("br"));
    // pay_detail.appendChild(host_fee_div);
    // pay_detail.appendChild(document.createElement("br"));
    pay_detail.appendChild(total_pay2);
    pay_detail.appendChild(detail_hr2);
    pay_detail.appendChild(document.createElement("br"));
    pay_detail.appendChild(total_pay3);

    // pay_detail_div (div)
    var pay_detail_div = document.getElementById("pay_detail_div");
    pay_detail_div.appendChild(pay_detail);
  }

  // 뒤로가기 이벤트 추가
  $("#shtbipdc1").on("click", function () {
    document.querySelector(".pay_detail1").className = "pay_detail1";
  });

  $("#shtbipdc2").on("click", function () {
    document.querySelector(".pay_detail2").className = "pay_detail2";
  });

  $("#shtbipdc3").on("click", function () {
    document.querySelector(".pay_detail3").className = "pay_detail3";
  });
}

//------------------------------------------------------------------------

// 결제 하기 버튼 이벤트
function pay_done_btn() {
  alert("결제완료");
  console.log("결제완료");
}


async function fetchUserInfo() {
  //  서버로부터 사용자 정보를 불러오는 함수
  const response = await fetch("/members/info");
  return await response.json();
}

async function requestPay() {
  //결제 창 보여주고 결제 정보에 사용자 정보를 담음.
  const userInfo = await fetchUserInfo();
  let price = 0;
  let menu = "항목 : ";

  $.ajax({
    url: "/basket/personal",
    type : "GET",
    success : function(response) {
      if (response != null ) {
        for(let i = 0; i < response.length; i++) {
          price += (response[i].price * response[i].quantity);
          if (i !== response.length -1 ) {
            menu += response[i].product_name + ", ";
          } else {
            menu += response[i].product_name;
          }
        }
        // 결제 창 호출
        var IMP = window.IMP;
        IMP.init("imp80525881");

        IMP.request_pay(
            {
              pg: "kakaopay",
              pay_method: "card",
              merchant_uid: "test_" + new Date().getTime(),
              name: menu,
              amount: price,
              buyer_tel: userInfo.pnum,
              buyer_name: userInfo.username,
              buyer_addr : userInfo.address
            }, async function (rsp) {
              console.log(rsp);
              $.ajax({
                url: "/payment/save",
                type: "POST",
                data: {
                  "mozipId" : id,
                  "menu" : rsp.name,
                  "totalPrice" : rsp.paid_amount,
                  "payStatus" : rsp.success
                },
                success: function (response) {
                  // 오류 시 처리
                  alert(response.responseText);
                },
                error: function(response) {
                  // 오류 시 처리
                  alert(response.responseText);
                }
              })
            }
        );
      } else {
        alert("장바구니에 메뉴를 담으세요!");
      }
    },
    error: function(response) {
      // 오류 시 처리
      alert(response.responseText);
    }
  });


}

// 장바구니 이동 2 try
const modalHeader = document.querySelector(".basket-title_topper_text");
const modalDialog = document.querySelector(".basket-view");
let isDragging = false;
let mouseOffset = { x: 0, y: 0 };
let dialogOffset = { left: 0, right: 0 };

modalHeader.addEventListener("mousedown", function (event) {
  isDragging = true;
  mouseOffset = { x: event.clientX, y: event.clientY };
  dialogOffset = {
    left:
      modalDialog.style.left === ""
        ? 0
        : Number(modalDialog.style.left.replace("px", "")),
    right:
      modalDialog.style.top === ""
        ? 0
        : Number(modalDialog.style.top.replace("px", "")),
  };
});

document.addEventListener("mousemove", function (event) {
  if (!isDragging) {
    return;
  }
  let newX = event.clientX - mouseOffset.x;
  let newY = event.clientY - mouseOffset.y;

  modalDialog.style.left = `${dialogOffset.left + newX}px`;
  modalDialog.style.top = `${dialogOffset.right + newY}px`;
});

document.addEventListener("mouseup", function () {
  isDragging = false;
});

