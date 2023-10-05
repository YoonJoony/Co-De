// 주소 설정 완료 버튼 클릭시
function Address_commit() { }

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
});

document.querySelector("#modal_close_btn").addEventListener("click", close);

// 모집글 생성 버튼 클릭시
$(function () {
    function modalClose() {
        $("#modal").fadeOut();
    }

    $("#create").click(function () {
        // 생성 & DB 저장

        // 백앤드 화이팅

        modalClose(); // 모달 닫기 함수 호출
    });
    $("#createOpen").click(function () {
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
// const elements = document.querySelectorAll(".board");
// const rowHeight = 615.56;
// var i = 0;
// elements.forEach((element, index) => {
//     const row = Math.floor(index / 4); //현재 요소가 속한 행 번호

//     var left = parseFloat(element.style.left); //left값을 받아온다. 처음에는 0. 두번째는 24.9617
//     //const top = parseFloat(element.style.top);

//     // 비율에 맞게 조정

//     if (i % 4 == 0) {
//         i = 0;
//     }

//     const newLeft = i * 25;
//     const newTop = row * rowHeight; //해당 해의 top 값이 223씩 증가

//     // 새로운 left와 top 값을 설정
//     element.style.left = `${newLeft}%`;
//     element.style.top = `${newTop}px`;
//     i++;
// });

//스크롤 할 시 게시물 추가로 보여짐
$(function () {
    $(".board").slice(0, 8).fadeIn(1000).css("display", "inline-block"); // 초기갯수

    $(window).scroll(function () {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
            console.log("스크롤바 끝 도달");

            if ($(".board:hidden").length == 0) {
                // 컨텐츠 남아있는지 확인
                // alert("게시물의 끝입니다."); // 컨텐츠 없을시 alert 창 띄우기
            }
            $(".board:hidden")
                .slice(0, 4)
                .fadeIn(1000)
                .css("display", "inline-block"); // 클릭시 more 갯수 지정
        }
    });
    // 클릭시 more
});

//새로고침시 스크롤 위치 초기화
history.scrollRestoration = "manual";

// 카테고리 슬라이드
const swiper = new Swiper('.swiper-container', {
    //기본 셋팅
    //방향 셋팅 vertical 수직, horizontal 수평 설정이 없으면 수평
    direction: 'horizontal',
    //한번에 보여지는 페이지 숫자
    slidesPerView: 'auto',

    //페이지와 페이지 사이의 간격
    spaceBetween: 10,

    //드레그 기능 true 사용가능 false 사용불가
    debugger: true,

    //마우스 휠기능 true 사용가능 false 사용불가
    mousewheel: true,

    //반복 기능 true 사용가능 false 사용불가
    loop: false,

    //선택된 슬라이드를 중심으로 true 사용가능 false 사용불가 djqt
    centeredSlides: false,

    threshold:100, //마우스 스와이프 민감도

    slidesOffsetAfter : 0

    // 페이지 전환효과 slidesPerView효과와 같이 사용 불가
    // effect: 'fade',

});

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
            // prompt: "배달음식을 시킬껀데 음식 추천해줘",
            messages: [
                {
                    role: "user", // 메시지 역할을 user로 설정
                    content: prompt, // 사용자가 입력한 메시지
                },
            ],
            temperature: 0.2, // 모델의 출력 다양성
            max_tokens: 300, // 응답받을 메시지 최대 토큰(단어) 수 설정
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

$(async function () {
    const message =
        "한국에서 배달 음식을 시킬껀데 나한테 질문 한 다음에 음식을 200자 이내로 추천해주고 음식에 대한 정보는 필요없어";

    const aiResponse = await fetchAIResponse(message);
    addMessage("챗봇", aiResponse);
});


// -------------------------------------------------------------------------------------------------------