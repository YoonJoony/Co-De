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

//위에 header를 스크롤 할 시 header fixed로 바뀌며가 자동으로 고정되게 하기
const header = document.querySelector(".header");
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
// ----------------------------------------

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
  const newTop = row * rowHeight; //해당 해의 top 값이 223씩 증가

  // 새로운 left와 top 값을 설정
  element.style.left = `${newLeft}%`;
  element.style.top = `${newTop}px`;
  i++;
});
// ----------------------------------------

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
function addMessage(sender, message) {
  // 새로운 div 생성
  const messageElement = document.createElement("div");
  // 생성된 요소에 클래스 추가
  messageElement.className = "message";
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
      Authorization: `Bearer ${apiKey}`,
    },
    body: JSON.stringify({
      model: "gpt-3.5-turbo", // 사용할 AI 모델
      messages: [
        {
          role: "user", // 메시지 역할을 user로 설정
          content: prompt, // 사용자가 입력한 메시지
        },
      ],
      temperature: 0.8, // 모델의 출력 다양성
      max_tokens: 1024, // 응답받을 메시지 최대 토큰(단어) 수 설정
      top_p: 1, // 토큰 샘플링 확률을 설정
      frequency_penalty: 0.5, // 일반적으로 나오지 않는 단어를 억제하는 정도
      presence_penalty: 0.5, // 동일한 단어나 구문이 반복되는 것을 억제하는 정도
      stop: ["Human"], // 생성된 텍스트에서 종료 구문을 설정
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
  addMessage("나", message);
  userInput.value = "";
  //ChatGPT API 요청후 답변을 화면에 추가
  const aiResponse = await fetchAIResponse(message);
  addMessage("챗봇", aiResponse);
});
// 사용자 입력 필드에서 Enter 키 이벤트를 처리
userInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    sendButton.click();
  }
});
