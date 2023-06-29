const id = document.querySelector('#id');
const pw = document.querySelector('#pw');
const loginButton = document.querySelector('#login-form-submit');
const loginErrorMsg = document.querySelector('#login-error-msg');

// 로그인 버튼 클릭시 예를 들어 id가 "user"이고 pw가 "1234"일 때만 로그인 성공
// 아닐 경우 숨겨진 오류 메시지 보이게 설정
loginButton.addEventListener("click", () => {
    if (id.value === "user" && pw.value === "1234") {
        alert("로그인 성공");
    } else {
        //숨겨진 오류 메시지 보이게 설정
        loginErrorMsg.style.opacity = 1;
    }
});