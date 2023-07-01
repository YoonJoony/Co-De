const id = document.querySelector('#id');
const pw = document.querySelector('#pw');
const loginButton = document.querySelector('#login-form-submit');
const loginErrorMsg = document.querySelector('#login-error-msg');

loginButton.addEventListener("click", () => {
    if (id.value === "user" && pw.value === "1234") {
        alert("로그인 성공");
    } else {
        alert("아이디 및 비밀번호를 다시 입력해주세요.");
        loginErrorMsg.style.opacity = 1;
    }
});

const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
  container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
  container.classList.remove("right-panel-active");
});

const nextButton = document.getElementById('next');
const forgotButton = document.getElementById('forgot');

nextButton.addEventListener('click', () => {
  container.classList.add("next-page");
  container.classList.add("out-page");
   setTimeout('gotoRegister()', 600); // 3초 후 실행
});

   function gotoRegister(){
       location.href="Register.html";  // 이동주소
   }

forgotButton.addEventListener('click', () => {
  container.classList.add("next-page");
  container.classList.add("out-page");
   setTimeout('gotoForgot()', 600); // 3초 후 실행
});

   function gotoForgot(){
       location.href="find_account.html";  // 이동주소
   }