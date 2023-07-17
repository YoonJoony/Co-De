function show_basket() {
  if (document.querySelector(".basket-view").className == "basket-view show") {
    document.querySelector(".basket-view").className = "basket-view hide";
  } else {
    document.querySelector(".basket-view").className = "basket-view show";
  }
}

document.querySelector(".basket-btn").addEventListener("click", show_basket);

function show_modal() {
  document.querySelector(".modal-background").className =
    "modal-background show-modal";
}
document
  .querySelector("#modify_menu_btn")
  .addEventListener("click", show_modal);

function close_modal() {
  document.querySelector(".modal-background").className = "modal-background";
}
document
  .querySelector(".modal-popup-close")
  .addEventListener("click", close_modal);
