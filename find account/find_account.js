// 휴대폰 번호 인증
function changePhone1(){
    const phone1 = document.getElementById("phone1").value // 010
    if(phone1.length === 3){
        document.getElementById("phone2").focus();
    }
}
function changePhone2(){
    const phone2 = document.getElementById("phone2").value // 010
    if(phone2.length === 4){
        document.getElementById("phone3").focus();
    }
}
function changePhone3(){
    const phone3 = document.getElementById("phone3").value // 010
    if(phone3.length === 4){
        document.getElementById("auth-button").focus();
        document.getElementById("auth-button").setAttribute("style","background-color:green;")
        document.getElementById("auth-button").disabled = false;
    }
}
function checkCompletion(){
    alert("문자 인증이 완료되었습니다.")
    initButton();
    document.getElementById("completion").innerHTML="인증완료"
    document.getElementById("signUpButton").disabled = false;
    document.getElementById("signUpButton").setAttribute("style","background-color:yellow;")
}
