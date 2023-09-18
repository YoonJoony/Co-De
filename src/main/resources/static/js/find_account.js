// 휴대폰 번호 인증
function changePhone1(){
    const phone1 = document.getElementById("phone1").value // 010
    if(phone1.length === 3){
        document.getElementById("phone2").focus();
    }
}
function changePhone2(){
    const phone2 = document.getElementById("phone2").value // 0000
    if(phone2.length === 4){
        document.getElementById("phone3").focus();
    }
}
function changePhone3(){
    const phone3 = document.getElementById("phone3").value // 0000
    if(phone3.length === 4){
        document.getElementById("auth-button").focus();
        document.getElementById("auth-button").disabled = false;
    }
}
// display: none으로 숨겨놨던 인증번호 입력 창을 보이게 설정
function getToken(){
    alert("인증번호가 전송 되었습니다")
    document.querySelectorAll(".auth-check").forEach(a=>a.style.display = "block");
}

// 인증확인을 눌렀을 때 인증번호가 맞는지 확인하는 메소드
function checkCompletion(){
    const auth_number = document.getElementById("auth-number").value
    if ( auth_number === "123456") {
        var PNum = auth_number
        alert("인증이 완료되었습니다.")
        localStorage.setItem('PNum', auth_number);
        location.href = "result_account.html";
    } else{
        alert("인증번호가 올바르지 않습니다.")
    }
}