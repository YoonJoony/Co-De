

        // 인증번호 입력칸은 디폴트로 숨겨 놓음
        $("#certified").hide();


        // input 및 button 변수화
        let id = $('#login');
        let pw = $('#pw');
        let pwcheck = $('#pwcheck');
        let username = $('#username');
        let nickname = $('#nickname');
        let address = $('#address');
        let phone = $('#phone');
        let btnsend = $('#btnsend');
        let btnRegister = $('#btnRegister');
        let certified = $('#certified');


        // 입력칸을 모두 입력하고 인증번호 받기 버튼을 누르면 인증번호 입력 칸이 나타남
        var regex = / /gi;
        var pattern = /\s/g;
        var bool = false;

        // 입력칸에 모두 빈칸과 공백이 모두 없어야 가입하기 버튼이 정상동작함
        // + 인증번호 받기 버튼 클릭 안할 시 가입하기 동작 안함
        // + 입력칸에 모두 빈칸과 공백이 모두 없어야 인증번호 받기 버튼이 동작함. -> form 요청 이벤트를 하나로 묶음(가입하기, 인증번호 받기)
         function jbSubmit(event) {

            var string = [id.val(), pw.val(), pwcheck.val(), username.val(), nickname.val(), address.val()];

            if(string[0] == "" || string[0].match(pattern)){
                if(string[0] != ""){
                    alert("아이디는 띄어쓰기가 입력될 수 없습니다.");
                }
                id.value = null;
                id.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                id.focus();
                return false;
            }else if(string[1] == "" || string[1].match(pattern)){
                if(string[1] != ""){
                    alert("비밀번호는 띄어쓰기가 입력될 수 없습니다.");
                }
                pw.value = null;
                pw.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                pw.focus();
                return false;
            }else if(string[2] != string[1]){
                alert("비밀번호 확인을 다시 입력해 주십시오.");
                pwcheck.value = null;
                pwcheck.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                pwcheck.focus();
                return false;
            }
            else if(string[3] == "" || string[3].match(pattern)){
                if(string[3] != ""){
                    alert("이름은 띄어쓰기가 입력될 수 없습니다.");
                }
                username.value = null;
                username.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                username.focus();
                return false;
            }
            else if(string[4] == "" || string[4].match(pattern)){
                if(string[4] != ""){
                    alert("닉네임은 띄어쓰기가 입력될 수 없습니다.");
                }
                nickname.value = null;
                nickname.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                nickname.focus();
                return false;
            }
            else if(address.val() == ""){
                address.value = null;
                address.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                address.focus();
                return false;
            }
            else if(phone.val() == ""){
                phone.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
                phone.focus();
                return false;
            }
            else if(!bool){
                alert("인증번호 받기 버튼을 클릭해주세요.");
                return false;
            }
            else  {
                container.classList.add("RegisterUp");
                $("#certified").show();
                if(certified.val() == "") {
                     alert("인증번호를 입력해주십시오.");
                     return false;
                }

                return true;
            }
        }

        btnsend.on('click', function() {
            bool = true;
        });


    const autoHyphen = (target) => {
     target.value = target.value
       .replace(/[^0-9]/g, '')
      .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
    }

    //아이콘 클릭시 다르게 바뀌게 설정
    $(document).ready(function(){
        $('.container i').on('click',function(){
            $('input').toggleClass('active');
            if($('input').hasClass('active')){
                $(this).attr('class',"fa-solid fa-eye")
                .prev('input').attr('type',"text");
            }else{
                $(this).attr('class',"fa-regular fa-eye-slash")
                .prev('input').attr('type','password');
            }
        });
    });