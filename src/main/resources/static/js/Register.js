

        // 인증번호 입력칸은 디폴트로 숨겨 놓음
        $("#certified").hide();


        // input 및 button 변수화
        let id = $('#id');
        let pw = $('#pw');
        let pw_check = $('#pw_check');
        let nickname = $('#nickname');
        let phone = $('#phone');
        let btnsend = $('#btnsend');
        let btnRegister = $('#btnRegister');

        // 입력칸을 모두 입력하고 인증번호 받기 버튼을 누르면 인증번호 입력 칸이 나타남
        btnsend.on('click', function() {
            if(id.val() == ""){
                id.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }else if(pw.val() == ""){
                pw.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }else if(pw_check.val() == ""){
                pw_check.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }
            else if(nickname.val() == ""){
                nickname.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }
            else if(phone.val() == ""){
                phone.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }else {
                container.classList.add("RegisterUp");
                $("#certified").show();
            }

        });

        // 입력칸에 모두 빈칸이 없어야 가입하기 버튼이 정상동작함
        btnRegister.on('click', function() {
            if(id.val() == ""){
                id.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }else if(pw.val() == ""){
                pw.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }else if(pw_check.val() == ""){
                pw_check.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }
            else if(nickname.val() == ""){
                nickname.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }
            else if(phone.val() == ""){
                phone.next('label').addClass('warning');
                setTimeout(function() {
                    $('label').removeClass('warning')
                }, 1500);
            }
        });

    const autoHyphen = (target) => {
     target.value = target.value
       .replace(/[^0-9]/g, '')
      .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
    }

