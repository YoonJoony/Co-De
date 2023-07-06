$(function(){
    $("#confirm").click(function(){
        modalClose();
    });
    $("#createOpen").click(function(){        
        $("#modal").css('display','flex').hide().fadeIn();
    });
    $("#close").click(function(){
        modalClose();
    });
    function modalClose(){
      $("#modal").fadeOut();
    }
});