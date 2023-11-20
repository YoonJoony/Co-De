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

const termsService = document.getElementById('termsService');
const termsPrivacy = document.getElementById('termsPrivacy');
const termsLocation = document.getElementById('termsLocation');

// sing up 눌렀을때 로그인 화면 넘기는 에니메이션 클래스 추가
const loginMove = document.getElementById('login_main');

signUpButton.addEventListener('click', () => {
  termsService.checked = false;
  termsPrivacy.checked = false;
  termsLocation.checked = false;
  container.classList.add("right-panel-active");
  loginMove.classList.add("move");
});

signInButton.addEventListener('click', () => {
  container.classList.remove("right-panel-active");
  loginMove.classList.remove("move");
});

const nextButton = document.getElementById('next');
const forgotButton = document.getElementById('forgot');


nextButton.addEventListener('click', () => {
  if(termsService.checked && termsPrivacy.checked && termsLocation.checked) {
      container.classList.add("next-page");
      container.classList.add("out-page");
      setTimeout('gotoRegister()', 600); // 3초 후 실행
      }
   else {
      alert("이용 약관을 전부 체크 해주세요.");
   }
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

