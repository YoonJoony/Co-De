// 장바구니 보여주는 함수
function show_basket() {
  if (document.querySelector(".basket-view").className == "basket-view show") {
    document.querySelector(".basket-view").className = "basket-view hide";
  } else {
    document.querySelector(".basket-view").className = "basket-view show";
  }
}
// 장바구니 버튼 클릭시 장바구니 보여주는 show_basket함수 호출
document.querySelector(".basket-btn").addEventListener("click", show_basket);

// 모달창 보여주는 함수
function show_modal() {
  document.querySelector(".modal-background").className =
    "modal-background show-modal";
}
// 메뉴 수정 버튼 클릭 시 show_modal함수 호출
document
  .querySelector("#modify_menu_btn")
  .addEventListener("click", show_modal);

// 모달 창 닫기
function close_modal() {
  document.querySelector(".modal-background").className = "modal-background";
}
// x클릭 시 close_modal 함수 호출
document
  .querySelector(".modal-popup-close")
  .addEventListener("click", close_modal);
