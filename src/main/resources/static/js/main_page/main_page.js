// 주소 설정 완료 버튼 클릭시
//모바일 햄버거 창 클릭 시 보이고 사라지게
function show() {
  document.querySelector(".header2").className = "header2 header2_show";
}

function close() {
  document.querySelector(".header2").className = "header2";
}

document.querySelector("#header_show").addEventListener("click", show);
document.querySelector("#header_close").addEventListener("click", close);

var stompClient = null;

function onConnected() {
  console.log("연결 성공!");
  stompClient.subscribe("/sub/mozip/chat/invite", onMessageReceived);
}

function onMessageReceived(payload) {
  console.log("onMessage");
  var invite = JSON.parse(payload.body);
}

function onError() {
  console.log("연결 실패ㅜㅜ");
}

$(function () {
  //소켓 설정. 채팅 방에서 누군가가 초대를 보낼 때 알림 받기 위해 소켓 연결
  var socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);

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

  $(".board-list").slice(0, 5).css("display", "block"); // 초기갯수
  $(".load-btn").click(function (e) {
    // 클릭시 more
    e.preventDefault();
    if ($(".board-list:hidden").length == 0) {
      // 컨텐츠 남아있는지 확인
      alert("게시물의 끝입니다."); // 컨텐츠 없을시 alert 창 띄우기
    }
    $(".board-list:hidden").slice(0, 5).css("display", "block"); // 클릭시 more 갯수 지정
  });
});

document.querySelector("#modal_close_btn").addEventListener("click", close);

//모집글 생성 버튼 클릭시
$(function () {
  function modalClose() {
    $("#modal").fadeOut();
  }

  $("#create").click(function () {
    // 생성 & DB 저장

    // 백앤드 화이팅

    modalClose(); // 모달 닫기 함수 호출
  });
  $(".v-bar").click(function () {
    $("#modal").css("display", "flex").hide().fadeIn(); // 속성 변경 후 hide로 숨기고 fadeIn으로 효과 나타내기
  });
  $("#close").click(function () {
    modalClose(); // 모달 닫기 함수 호출
  });
});

// selectbox js

/* ===== Logic for creating fake Select Boxes ===== */
$(".sel").each(function () {
  $(this).children("select").css("display", "none");

  var $current = $(this);

  $(this)
    .find("option")
    .each(function (i) {
      if (i == 0) {
        $current.prepend(
          $("<div>", {
            class: $current.attr("class").replace(/sel/g, "sel__box"),
          })
        );

        var placeholder = $(this).text();
        $current.prepend(
          $("<span>", {
            class: $current.attr("class").replace(/sel/g, "sel__placeholder"),
            text: placeholder,
            "data-placeholder": placeholder,
          })
        );

        return;
      }

      $current.children("div").append(
        $("<span>", {
          class: $current.attr("class").replace(/sel/g, "sel__box__options"),
          text: $(this).text(),
        })
      );
    });
});

// Toggling the `.active` state on the `.sel`.
$(".sel").click(function () {
  $(this).toggleClass("active");
});

// Toggling the `.selected` state on the options.
$(".sel__box__options").click(function () {
  var txt = $(this).text();
  var index = $(this).index();

  $(this).siblings(".sel__box__options").removeClass("selected");
  $(this).addClass("selected");

  var $currentSel = $(this).closest(".sel");
  $currentSel.children(".sel__placeholder").text(txt);
  $currentSel.children("select").prop("selectedIndex", index + 1);
});

//위에 header를 스크롤 할 시 header fixed로 바뀌며가 자동으로 고정되게 하기
const header = document.querySelector(".header2");
var $topper = $(".topper");
var $window = $(window);

// 컨텐츠 영역부터 브라우저 최상단까지의 길이 구하기
const contentTop = header.getBoundingClientRect().top + window.scrollY;

window.addEventListener("scroll", function () {
  if ($window.scrollTop() > $topper.height()) {
    header.classList.add("fixed");
  } else {
    header.classList.remove("fixed");
  }
});

// ----------------------------------------------------------------------

// 노출 거리 설정
$("#header_board_range_btn").click(function () {
  if ($(".header_rangeInput_div").css("visibility") == "hidden") {
    $(".header_rangeInput_div").css("visibility", "visible");
  } else {
    $(".header_rangeInput_div").css("visibility", "hidden");
  }
});

$(function () {
  // -------------글 노출 거리 검색 슬라이더------------
  let rangeInput = document.getElementById("header_rangeInput");

  let distances = [0, 100, 300, 500];
  let selectedDistance;

  rangeInput.addEventListener("input", function (e) {
    selectedDistance = distances[e.target.value];
    mozipRange = selectedDistance;
    console.log(`Selected distance: ${selectedDistance}m`);
  });
});

//페이지 이동 .. 잡 처리
var $mozipPage = $(".mozipPage");

$mozipPage.click(function () {
  location.href = "/main_page.html";
});

//내 채팅 누를 시 내 채팅방 이동
async function myChat() {
  try {
    const response = await $.ajax({
      type: "GET",
      url: "/mozip/chat/myChatroom",
      data: {},
    });
    location.href = "/mozip/chat/room?id=" + response;
  } catch (error) {
    console.log("내 채팅 접속 요청 실패");
  }
}
//------------------------------------------------------------------------

//모집글 board 영역 설정
const elements = document.querySelectorAll(".board");
const rowHeight = 615.56;
var i = 0;
elements.forEach((element, index) => {
  const row = Math.floor(index / 4); //현재 요소가 속한 행 번호

  var left = parseFloat(element.style.left); //left값을 받아온다. 처음에는 0. 두번째는 24.9617
  //const top = parseFloat(element.style.top);

  // 비율에 맞게 조정

  if (i % 4 == 0) {
    i = 0;
  }

  const newLeft = i * 25;
  const newTop = row * rowHeight; //해당 해의 top 값이 615 증가

  // 새로운 left와 top 값을 설정
  element.style.left = `${newLeft}%`;
  element.style.top = `${newTop}px`;
  i++;
});
// ----------------------------------------

// 챗봇-----------------------------------------------

// 채팅 메시지를 표시할 DOM
const chatMessages = document.querySelector("#chat-messages");
// 사용자 입력 필드
const userInput = document.querySelector("#user-input input");
// 전송 버튼
const sendButton = document.querySelector("#user-input button");
// 발급받은 OpenAI API 키를 변수로 저장
const apiKey = "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";
// OpenAI API 엔드포인트 주소를 변수로 저장
const apiEndpoint = "https://api.openai.com/v1/chat/completions";

//user 메시지
function addMessage_user(sender, message) {
  // 새로운 div 생성
  const messageElement = document.createElement("div");
  // 생성된 요소에 클래스 추가
  messageElement.className = "message_user";
  // 채팅 메시지 목록에 새로운 메시지 추가
  messageElement.textContent = `${sender}: ${message}`;
  chatMessages.prepend(messageElement);
}

// gpt 메시지
function addMessage_gpt(sender, message) {
  // 새로운 div 생성
  const messageElement = document.createElement("div");
  // 생성된 요소에 클래스 추가
  messageElement.className = "message_gpt";
  // 채팅 메시지 목록에 새로운 메시지 추가
  messageElement.textContent = `${sender}: ${message}`;
  chatMessages.prepend(messageElement);
}

// ChatGPT API 요청
async function fetchAIResponse(prompt) {
  // API 요청에 사용할 옵션을 정의
  const requestOptions = {
    method: "POST",
    // API 요청의 헤더를 설정
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${"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"}`,
    },
    body: JSON.stringify({
      model: "gpt-3.5-turbo", // 사용할 AI 모델
      // prompt: "배달음식을 시킬껀데 음식 추천해줘",
      messages: [
        {
          role: "system",
          content:
            "The user is trying to order delivery food and you have to recommend a food menu." +
            " And we can only talk about menu recommendations." +
            " You have to answer in Korean only." +
            " You have to answer it concisely.",
        },
        {
          role: "user", // 메시지 역할을 user로 설정
          content: prompt, // 사용자가 입력한 메시지
        },
      ],
      temperature: 0, // 모델의 출력 다양성
      max_tokens: 256, // 응답받을 메시지 최대 토큰(단어) 수 설정
      top_p: 1, // 토큰 샘플링 확률을 설정
      frequency_penalty: 0, // 일반적으로 나오지 않는 단어를 억제하는 정도
      presence_penalty: 0, // 동일한 단어나 구문이 반복되는 것을 억제하는 정도
      // stop: ["Human"], // 생성된 텍스트에서 종료 구문을 설정
    }),
  };
  // API 요청후 응답 처리
  try {
    const response = await fetch(apiEndpoint, requestOptions);
    const data = await response.json();
    const aiResponse = data.choices[0].message.content;
    return aiResponse;
  } catch (error) {
    console.error("OpenAI API 호출 중 오류 발생:", error);
    return "OpenAI API 호출 중 오류 발생";
  }
}

// 전송 버튼 클릭 이벤트 처리
sendButton.addEventListener("click", async () => {
  // 사용자가 입력한 메시지
  const message = userInput.value.trim();
  // 메시지가 비어있으면 리턴
  if (message.length === 0) return;
  // 사용자 메시지 화면에 추가
  addMessage_user("나", message);
  userInput.value = "";
  //ChatGPT API 요청후 답변을 화면에 추가
  const aiResponse = await fetchAIResponse(message);
  addMessage_gpt("챗봇", aiResponse);
});
// 사용자 입력 필드에서 Enter 키 이벤트를 처리
userInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    sendButton.click();
  }
});

$(async function () {
  const message =
    "한국에서 배달 음식을 시킬껀데 나한테 질문 한 다음에 음식을 200자 이내로 추천해주고 음식에 대한 정보는 필요없어";

  const aiResponse = await fetchAIResponse(message);
  addMessage("챗봇", aiResponse);
});

function chatbot_open() {
  const chatbot_content = document.querySelector(".main-right");

  // 숨기기 (display: none)
  if (chatbot_content.style.display !== "block") {
    $("html").scrollTop(0);
    chatbot_content.style.display = "block";
  }
  // 보이기 (display: block)
  else {
    chatbot_content.style.display = "none";
  }
}

// 모바일 채팅창 표시
function chat_show() {
  document.querySelector(".width-right").className =
    "width-right chatbot_show chat_background";
}

function chat_close() {
  document.querySelector(".width-right").className = "width-right";
}

document.querySelector("#chat_show").addEventListener("click", chat_show);
document.querySelector("#chat_close").addEventListener("click", chat_close);
