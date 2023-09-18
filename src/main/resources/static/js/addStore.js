const search_store_btn = document.querySelector("#add_store");
search_store_btn.addEventListener("click", showStoreList);

var storeModal_body = document.querySelector(".storeModal_body");

function showStoreList() {
  while(storeModal_body.firstChild) {
    storeModal_body.removeChild(storeModal_body.firstChild);
  }
  var con = document.querySelector(".storeModal");
  if (con.style.display == "block") {
    con.style.display = "none";
  } else {
    con.style.display = "block";
  }

 $.ajax({
        type : "GET",
        url : "/mozip/storeList",
        data : {
        },
        success: function(data) {
            var storeModalClose = document.createElement("button");
            storeModalClose.classList.add("storeModal_close");
            var storeModalCloseName = document.createTextNode("닫기"); // 크롤링 데이터로 가져온 가게 이름 넣어야함
            storeModalClose.appendChild(storeModalCloseName); // store_list의 자식으로 store_image_div;
            storeModalClose.setAttribute("onClick", "closeStoreList()");

            storeModal_body.appendChild(storeModalClose);
            for(let i = 0; i < data.length; i++) {
                var store_image = document.createElement("img");
                  store_image.classList.add("store_image");
                  // 백앤드 데이터 가져오기
                  store_image.setAttribute("src", data[i].imageUrl); // 이미지 소스 추가해야함

                  // store_image_div
                  var store_image_div = document.createElement("div");
                  store_image_div.classList.add("store_image_div"); // 클래스 이름 store_image_div으로 설정
                  store_image_div.appendChild(store_image); // store_info_div의 자식으로 store_images

                  // store_minimum_order_amount
                  var store_minimum_order_amount = document.createElement("div");
                  store_minimum_order_amount.classList.add("store_minimum_order_amount"); // 클래스 이름 store_minimum_order_amount으로 설정
                  // 백앤드 데이터 가져오기
                  //------------------------------------------------------------------------------------------
                  var minimumOrderData = document.createTextNode("최소주문금액 : " +  data[i].minPrice); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_minimum_order_amount.appendChild(minimumOrderData); // store_minimum_order_amount의 자식으로 최소주문가격 데이터
                  //-----------------------------------------------------------------------------------------

                  // store_name
                  var store_name = document.createElement("div");
                  store_name.classList.add("store_name"); // 클래스 이름 store_name으로 설정
                  // 백앤드 데이터 가져오기
                  //------------------------------------------------------------------------------------------
                  var storeName_data = document.createTextNode(data[i].title); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_name.appendChild(storeName_data); // bell_nickname의 자식으로 가게 이름 데이터
                  //-----------------------------------------------------------------------------------------

                  // store_info_div
                  var store_info_div = document.createElement("div");
                  store_info_div.classList.add("store_info_div"); // 클래스 이름 store_info_div으로 설정
                  store_info_div.appendChild(store_name); // store_info_div의 자식으로 store_name
                  store_info_div.appendChild(store_minimum_order_amount); // store_info_div의 자식으로 store_minimum_order_amount

                  // store_list
                  var store_list = document.createElement("div");
                  store_list.classList.add("store_list"); // 클래스 이름 store_list으로 설정
                  store_list.appendChild(store_image_div); // store_list의 자식으로 store_image_div
                  store_list.appendChild(store_info_div); // store_list의 자식으로 store_info_div


                  // storeModal_body
                  storeModal_body.appendChild(store_list);
              }

        },
        error: function() {
            console.log("리스트 요청 실패 : ");
        }
    })
}

function closeStoreList() {
  var con = document.querySelector(".storeModal");
  if (con.style.display == "block") {
    con.style.display = "none";
  }
}


function store_load() {
  console.log("store_load");
  // var nickname = JSON.parse(payload.body); // ex)엄준식
  // payload는 클라이언트에서 수신한 메시지를 나타냄.
  // 하지만 클라이언트가 수신한건지 서버로 송신한 메시지인지 모르니 payload.body로 수신한 메시지를 확인하는 로직을 써줌

  // store_image
  var store_image = document.createElement("img");
  store_image.classList.add("store_image");
  // 백앤드 데이터 가져오기
  store_image.setAttribute("src", "images/profile.png"); // 이미지 소스 추가해야함

  // store_image_div
  var store_image_div = document.createElement("div");
  store_image_div.classList.add("store_image_div"); // 클래스 이름 store_image_div으로 설정
  store_image_div.appendChild(store_image); // store_info_div의 자식으로 store_images

  // store_minimum_order_amount
  var store_minimum_order_amount = document.createElement("div");
  store_minimum_order_amount.classList.add("store_minimum_order_amount"); // 클래스 이름 store_minimum_order_amount으로 설정
  // 백앤드 데이터 가져오기
  //------------------------------------------------------------------------------------------
  var minimumOrderData = document.createTextNode("최소주문금액 : " + "data"); // 크롤링 데이터로 가져온 가게 이름 넣어야함
  store_minimum_order_amount.appendChild(minimumOrderData); // store_minimum_order_amount의 자식으로 최소주문가격 데이터
  //-----------------------------------------------------------------------------------------

  // store_name
  var store_name = document.createElement("div");
  store_name.classList.add("store_name"); // 클래스 이름 store_name으로 설정
  // 백앤드 데이터 가져오기
  //------------------------------------------------------------------------------------------
  var storeName_data = document.createTextNode("가게이름 data"); // 크롤링 데이터로 가져온 가게 이름 넣어야함
  store_name.appendChild(storeName_data); // bell_nickname의 자식으로 가게 이름 데이터
  //-----------------------------------------------------------------------------------------

  // store_info_div
  var store_info_div = document.createElement("div");
  store_info_div.classList.add("store_info_div"); // 클래스 이름 store_info_div으로 설정
  store_info_div.appendChild(store_name); // store_info_div의 자식으로 store_name
  store_info_div.appendChild(store_minimum_order_amount); // store_info_div의 자식으로 store_minimum_order_amount

  // store_list
  var store_list = document.createElement("div");
  store_list.classList.add("store_list"); // 클래스 이름 store_list으로 설정
  store_list.appendChild(store_image_div); // store_list의 자식으로 store_image_div
  store_list.appendChild(store_info_div); // store_list의 자식으로 store_info_div

  // storeModal_body
  storeModal_body.appendChild(store_list);
}
