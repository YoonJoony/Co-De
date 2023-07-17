function show() {
  if (document.querySelector(".basket-view").className == "basket-view show") {
    document.querySelector(".basket-view").className = "basket-view hide";
  } else {
    document.querySelector(".basket-view").className = "basket-view show";
  }
}

document.querySelector(".basket-btn").addEventListener("click", show);
