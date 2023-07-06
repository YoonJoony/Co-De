$(function(){
    function modalClose(){
        $("#modal").fadeOut();
      }

      
    $("#create").click(function(){ 
        // 생성 & DB 저장
        
        // 백앤드 화이팅

        modalClose(); // 모달 닫기 함수 호출
    });
    $("#createOpen").click(function(){        
        $("#modal").css('display','flex').hide().fadeIn(); // 속성 변경 후 hide로 숨기고 fadeIn으로 효과 나타내기
    });
    $("#close").click(function(){
        modalClose(); // 모달 닫기 함수 호출
    });
});