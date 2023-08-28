// 카테고리
var MainSwiper = new Swiper('.main_swiper .swiper-container', {

    slidesPerView: 4, // 동시에 보여줄 슬라이드 갯수
    spaceBetween: 30, // 슬라이드간 간격
    loop: true, // 무한 반복

    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
    pagination: {
        el: '.swiper-pagination',
        type: 'bullets',
        clickable: true,
    },
});

var SubSwiper = new Swiper('.sub_swiper .swiper-container', {
    slidesPerView: 4, // 동시에 보여줄 슬라이드 갯수
    spaceBetween: 30, // 슬라이드간 간격
    loop: true, // 무한 반복

    pagination: {
        el: '.swiper-pagination',
        type: 'bullets',
        clickable: true,
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
});

MainSwiper.controller.control = SubSwiper;
SubSwiper.controller.control = MainSwiper;
// ----------------------------------------------

function profile_btn() {
    const box = document.getElementById("layer-header-profile");

    // btn1 숨기기 (display: none)
    if (box.style.display !== "block") {
        box.style.display = "block";
    }
    // btn` 보이기 (display: block)
    else {
        box.style.display = "none";
    }
}

// 마이페이지 메뉴 선택시 
function myPage() {

}

// 참여중인 모집글 메뉴 선택시
function joining() {

}

// 결제내역 조회 선택시
function credit() {

}

// ----------------------------------------------