const id = document.querySelector('#id');
const pw = document.querySelector('#pw');
const loginButton = document.querySelector('#login-form-submit');
const loginErrorMsg = document.querySelector('#login-error-msg');

loginButton.addEventListener("click", () => {
    if (id.value === "user" && pw.value === "1234") {
        alert("로그인 성공");
    } else {
        loginErrorMsg.style.opacity = 1;
    }
});