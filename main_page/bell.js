function Bell_display() {
  var con = document.querySelector(".bell-modal");
  if (con.style.display == "none") {
    con.style.display = "block";
  } else {
    con.style.display = "none";
    console.log("오류");
  }
}

var bellArea = document.querySelector(".bell-modal");

function onBellReceived() {
  console.log("onbell");
  // var nickname = JSON.parse(payload.body); // ex)엄준식
  // payload는 클라이언트에서 수신한 메시지를 나타냄.
  // 하지만 클라이언트가 수신한건지 서버로 송신한 메시지인지 모르니 payload.body로 수신한 메시지를 확인하는 로직을 써줌

  var bell_no = document.createElement("button");
  bell_no.classList.add("bell-no"); // 클래스 이름 bell_no로 설정
  var bell_no_text = document.createTextNode("거절");
  bell_no.appendChild(bell_no_text); // bell_no 자식으로 bell_no_text 텍스트

  var bell_yes = document.createElement("button");
  bell_yes.classList.add("bell-yes"); // 클래스 이름 bell_yes로 설정
  var bell_yes_text = document.createTextNode("수락");
  bell_yes.appendChild(bell_yes_text); // bell_yes 자식으로 bell_yes_text 텍스트

  var bell_btn_div = document.createElement("div");
  bell_btn_div.classList.add("bell-btn-div"); // 클래스 이름 bell_btn_div으로 설정
  bell_btn_div.appendChild(bell_yes); // bell_btn_div의 자식으로 bell_yes
  bell_btn_div.appendChild(bell_no); // bell_btn_div의 자식으로 bell_no

  var bell_nickname = document.createElement("div");
  bell_nickname.classList.add("bell-nickname"); // 클래스 이름 bell_list으로 설정
  // 백앤드 데이터 가져와서 text 넣기
  //------------------------------------------------------------------------------------------
  var nickname_data = document.createTextNode("엄준식" + "님의 초대"); // ex) 엄준식 | chat 넣어야함
  bell_nickname.appendChild(nickname_data); // bell_nickname의 자식으로 닉네임 데이터
  //-----------------------------------------------------------------------------------------

  var bell_list = document.createElement("div");
  bell_list.classList.add("bell-list"); // 클래스 이름 bell_list으로 설정
  bell_list.appendChild(bell_nickname); // bell_list의 자식으로 bell_nickname
  bell_list.appendChild(bell_btn_div); // bell_list의 자식으로 bell_btn

  bellArea.appendChild(bell_list);
}
