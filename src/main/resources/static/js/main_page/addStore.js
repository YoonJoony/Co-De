const search_store_btn = document.querySelector("#add_store");
const search_store_input_btn = document.querySelector(".storeName-input");
search_store_btn.addEventListener("click", showStoreList);
search_store_input_btn.addEventListener("click", showStoreList);

var storeModal_body = document.querySelector(".storeModal_body");
var loading_div = document.querySelector(".loading-div");

var category;
var choiceStore = null;

let count = 0;
function showStoreList() {
  category = null;

  var con = document.querySelector(".storeModal");
  con.style.display = "block";


  selenium();
}

// 모집글 생성 시 가게 선택을 누를 경우 현재 등록된 주소 기준으로 가게 크롤링
function selenium() {
  while(storeModal_body.firstChild) {
    storeModal_body.removeChild(storeModal_body.firstChild);
  }

  loading_div.style.display = "block";

    $.ajax({
        type : "GET",
        url : "/mozip/storeList",
        data : {

        },
        success: function(data) {
            for(let i = 0; i < data.length; i++) {
                var store_image = document.createElement("img");
                  store_image.classList.add("store_image");
                  // 백앤드 데이터 가져오기
                  store_image.setAttribute("src", data[i].imageUrl); // 이미지 소스 추가해야함

                  // store_image_div
                  var store_image_div = document.createElement("div");
                  store_image_div.classList.add("store_image_div"); // 클래스 이름 store_image_div으로 설정
                  store_image_div.appendChild(store_image); // store_info_div의 자식으로 store_images

                  //store_info_div_top div(제목)---------------------------------
                  var store_info_div_top = document.createElement("div");

                  //제목
                  var store_name = document.createElement("div");
                  store_name.classList.add("store_name"); // 클래스 이름 store_name으로 설정
                  var store_name_text = document.createTextNode(data[i].title); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_name.appendChild(store_name_text);
                  store_info_div_top.appendChild(store_name);

                  //store_info_div_bottom div(별점, 리뷰개수...)---------------------------------
                  var store_info_div_bottom = document.createElement("div");
                  store_info_div_bottom.classList.add("store_info_div_bottom"); // 클래스 이름 store_name으로 설정

                  //별점
                  var span = document.createElement("span");
                  var ico_Star = document.createElement("span");
                  ico_Star.classList.add("ico-star"); // 클래스 이름 store_image_div으로 설정
                  var ico_Star_text = document.createTextNode(data[i].icoStar);
                  ico_Star.appendChild(ico_Star_text);
                  span.appendChild(ico_Star);

                  //리뷰 개수
                  var review_num = document.createElement("div");
                  review_num.classList.add("review_num"); // 클래스 이름 store_image_div으로 설정
                  var review_num_text = document.createTextNode(data[i].review_num);
                  review_num.appendChild(review_num_text);

                  // 최소주문금액
                  var store_minimum_order_amount = document.createElement("div");
                  store_minimum_order_amount.classList.add("store_minimum_order_amount"); // 클래스 이름 store_minimum_order_amount으로 설정
                  var minimumOrderData = document.createTextNode(data[i].minPrice); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_minimum_order_amount.appendChild(minimumOrderData); // store_minimum_order_amount의 자식으로 최소주문가격 데이터

                  store_info_div_bottom.appendChild(span);
                  store_info_div_bottom.appendChild(review_num);
                  store_info_div_bottom.appendChild(store_minimum_order_amount);


                  // store_info_div
                  var store_info_div = document.createElement("div");
                  store_info_div.classList.add("store_info_div"); // 클래스 이름 store_info_div으로 설정
                  store_info_div.appendChild(store_info_div_top); // store_info_div의 자식으로 store_name
                  store_info_div.appendChild(store_info_div_bottom); // store_info_div의 자식으로 store_minimum_order_amount

                  //store_time_div
                  var store_time_div = document.createElement("div");
                  store_time_div.classList.add("store_time_div");
                  var store_time = document.createElement("div");
                  store_time.classList.add("store_time");
                  var store_time_text = document.createTextNode(data[i].deliveryTime);
                  store_time.appendChild(store_time_text);
                  store_time_div.appendChild(store_time);

                  // store_list
                  var store_list = document.createElement("div");
                  store_list.classList.add("store_list");
                  store_list.appendChild(store_image_div);
                  store_list.appendChild(store_info_div);
                  store_list.appendChild(store_time_div);

                  loading_div.style.display = "none";
                  // storeModal_body
                  storeModal_body.appendChild(store_list);
              }
              count = 1;
        },
        error: function() {
            console.log("리스트 요청 실패 : ");
        }
    })
}

// 가게 선택 메뉴 UI 창에서 카테고리 메뉴를 클릭할 경우 해당 카테고리의 요기요 가게리스트 페이지에서 가게 크롤링
function selenium_Category() {
  while(storeModal_body.firstChild) {
    storeModal_body.removeChild(storeModal_body.firstChild);
  }
  loading_div.style.display = "block";

    $.ajax({
        type : "GET",
        url : "/mozip/categoryList",
        data : {
          "category" : category
        },
        success: function(data) {
            for(let i = 0; i < data.length; i++) {
                var store_image = document.createElement("img");
                  store_image.classList.add("store_image");
                  // 백앤드 데이터 가져오기
                  store_image.setAttribute("src", data[i].imageUrl); // 이미지 소스 추가해야함

                  // store_image_div
                  var store_image_div = document.createElement("div");
                  store_image_div.classList.add("store_image_div"); // 클래스 이름 store_image_div으로 설정
                  store_image_div.appendChild(store_image); // store_info_div의 자식으로 store_images

                  //store_info_div_top div(제목)---------------------------------
                  var store_info_div_top = document.createElement("div");

                  //제목
                  var store_name = document.createElement("div");
                  store_name.classList.add("store_name"); // 클래스 이름 store_name으로 설정
                  var store_name_text = document.createTextNode(data[i].title); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_name.appendChild(store_name_text);
                  store_info_div_top.appendChild(store_name);

                  //store_info_div_bottom div(별점, 리뷰개수...)---------------------------------
                  var store_info_div_bottom = document.createElement("div");
                  store_info_div_bottom.classList.add("store_info_div_bottom"); // 클래스 이름 store_name으로 설정

                  //별점
                  var span = document.createElement("span");
                  var ico_Star = document.createElement("span");
                  ico_Star.classList.add("ico-star"); // 클래스 이름 store_image_div으로 설정
                  var ico_Star_text = document.createTextNode(data[i].icoStar);
                  ico_Star.appendChild(ico_Star_text);
                  span.appendChild(ico_Star);

                  //리뷰 개수
                  var review_num = document.createElement("div");
                  review_num.classList.add("review_num"); // 클래스 이름 store_image_div으로 설정
                  var review_num_text = document.createTextNode(data[i].review_num);
                  review_num.appendChild(review_num_text);

                  // 최소주문금액
                  var store_minimum_order_amount = document.createElement("div");
                  store_minimum_order_amount.classList.add("store_minimum_order_amount"); // 클래스 이름 store_minimum_order_amount으로 설정
                  var minimumOrderData = document.createTextNode(data[i].minPrice); // 크롤링 데이터로 가져온 가게 이름 넣어야함
                  store_minimum_order_amount.appendChild(minimumOrderData); // store_minimum_order_amount의 자식으로 최소주문가격 데이터

                  store_info_div_bottom.appendChild(span);
                  store_info_div_bottom.appendChild(review_num);
                  store_info_div_bottom.appendChild(store_minimum_order_amount);


                  // store_info_div
                  var store_info_div = document.createElement("div");
                  store_info_div.classList.add("store_info_div"); // 클래스 이름 store_info_div으로 설정
                  store_info_div.appendChild(store_info_div_top); // store_info_div의 자식으로 store_name
                  store_info_div.appendChild(store_info_div_bottom); // store_info_div의 자식으로 store_minimum_order_amount

                  //store_time_div
                  var store_time_div = document.createElement("div");
                  store_time_div.classList.add("store_time_div");
                  var store_time = document.createElement("div");
                  store_time.classList.add("store_time");
                  var store_time_text = document.createTextNode(data[i].deliveryTime);
                  store_time.appendChild(store_time_text);
                  store_time_div.appendChild(store_time);

                  // store_list
                  var store_list = document.createElement("div");
                  store_list.classList.add("store_list");
                  store_list.appendChild(store_image_div);
                  store_list.appendChild(store_info_div);
                  store_list.appendChild(store_time_div);


                  loading_div.style.display = "none";
                  // storeModal_body
                  storeModal_body.appendChild(store_list);
              }
              count = 1;
        },
        error: function() {
            console.log("리스트 요청 실패 : ");
        }
    })
}

//미개한 방식이니 나중에 클릭 이벤트로 수정하기
function category_store_list(select_category){
  switch (select_category) {
    case "전체보기":
        category = select_category;
        selenium_Category();
        break;
    case "프랜차이즈":
        category = select_category;
        selenium_Category();
        break;
    case "치킨":
        category = select_category;
        selenium_Category();
        break;
    case "피자/양식":
        category = select_category;
        selenium_Category();
        break;
    case "중국집":
        category = select_category;
        selenium_Category();
        break;
    case "한식":
        category = select_category;
        selenium_Category();
         break;
    case "일식/돈까스":
        category = select_category;
        selenium_Category();
        break;
    case "족발/보쌈":
        category = select_category;
        selenium_Category();
        break;
    case "야식":
        category = select_category;
        selenium_Category();
        break;
    case "분식":
        category = select_category;
        selenium_Category();
        break;
    case "카페/디저트":
        category = select_category;
        selenium_Category();
        break;
    default:
      // 모든 case와 일치하지 않는 경우 실행되는 코드
  }
}






function closeStoreList() {
  var con = document.querySelector(".storeModal");
  if (con.style.display == "block") {
    con.style.display = "none";
  }
   $.ajax({
          type : "POST",
          url : "/mozip/closeDriver",
          data : {
          },
          success: function() {
            console.log("Driver quit()")
          },
          error: function() {
             console.log("Driver 종료 요청 실패 : ");
          }
    })
}

//가게선택 검색창 설정
const inputElement = document.querySelector(".search-txt");
const placeholderText = inputElement.placeholder;

inputElement.addEventListener("focus", function() {
  this.placeholder = "";
});

inputElement.addEventListener("blur", function() {
  this.placeholder = placeholderText;
});

//가게 선택 화면에서 카테고리 애니메이션
let scrollContainer = document.querySelector('.storeModal_header_categories');

let startX = 0;
let scrollLeft = 0;
let isDown = false; // Add this

scrollContainer.addEventListener('pointerdown', (e) => {
    isDown = true; // Set isDown to true when pointer is down
    startX = e.pageX - scrollContainer.offsetLeft;
    scrollLeft = scrollContainer.scrollLeft;
    scrollContainer.style.cursor = 'grabbing';
});

scrollContainer.addEventListener('pointerup', () => {
    isDown = false; // Set isDown to false when pointer is up
    scrollContainer.style.cursor = 'grab';
});

scrollContainer.addEventListener('pointerleave', () => {
    isDown = false; // Set isDown to false when pointer leaves the element
    scrollContainer.style.cursor = 'grab';
});

scrollContainer.addEventListener('pointermove', (e) => {
    if (!isDown) return; // Only run if pointer is down
    e.preventDefault();
    const x = e.pageX - scrollContainer.offsetLeft;
    const walk = (x - startX);
    scrollContainer.scrollLeft = scrollLeft - walk;
});

//------------------------------모집글 생성 부분------------------------------------------
var mozipTitle; //제목
var mozipRange = null; //거리
var mozipCategory = null; //카테고리
var mozipStore = null; //가게
var mozipPeople = null; //인원 수
var search_txt = document.querySelector(".search-txt"); //제목 input 요소
document.querySelector("#modal_close_beck").addEventListener("click", close);

//(아래 함수는 문서가 전부 로딩된 후 실행된다고 함)
$(function () {

    // -------------글 노출 거리 슬라이더------------
    let rangeInput = document.getElementById("rangeInput");

    let distances = [0, 100, 300, 500];
    let selectedDistance;

    rangeInput.addEventListener("input", function(e) {
        selectedDistance = distances[e.target.value];
        mozipRange = selectedDistance;
        console.log(`Selected distance: ${selectedDistance}m`);
    });

    // -------------선택한 카테고리 이름 불러오기------------
    $(document).on('click', '.category-item', function() {
        $(".category-item").css({ "background-color": "#fff", "color": "black" });
        $(this).css({ "background-color": "#ff8c16", "color": "#FFFFFF" });
        choiceCategory = $(this).text();
        mozipCategory = choiceCategory;
        console.log(choiceCategory);
    });

    // -------------선택한 가게 이름 불러오기------------
    $(document).on('click', '.store_list', function() {
        choiceStore = $(this).find('.store_name').text();
        mozipStore = choiceStore;
        $('#storeName-input').val(choiceStore);
        closeStoreList();
        console.log(choiceStore);
    });
});

// -------------모집글 생성 버튼 클릭 시------------
function createMozip() {
    //모집글 제목 저장
    mozipTitle = search_txt.value;
    //인원 선택한 값 저장
    mozipPeople = document.getElementById("people").value;
    console.log(mozipTitle + "///" + mozipRange + "///" + mozipCategory + "///" + mozipStore + "///" + mozipPeople);

    if(mozipTitle === "") {
        alert("제목을 작성 해주세요!")
        return;
    }else if(mozipTitle.length > 30) {
        alert("모집글 제목이 너무 길어요!")
        return;
    }else if(mozipCategory === null) {
        alert("키테고리를 선택 해주세요!")
        return;
    }else if(mozipStore === null) {
        alert("가게를 선택 해주세요!")
        return;
    }

    // ----------서버에 모집글 정보 전송------------
    $.ajax({
           type : "POST",
           url : "/mozip",
           data : {
                "mozipTitle" : mozipTitle,
                "mozipRange" : mozipRange,
                "mozipCategory" : mozipCategory,
                "mozipStore" : mozipStore,
                "mozipPeople" : mozipPeople,
           },
           success: function(mozip) {
               if (chkRoomUserCnt(mozip.id, mozip.nickname)) {
                   $.ajax({
                       type: "GET",
                       url: "/mozip/chat/room?id=" + mozip.id,
                       data: {
                       },
                       async: false,
                       success: function (result) {
                           location.href = "/mozip/chat/room?id=" + mozip.id;
                       },
                       error: function () {
                           alert("실패!");
                       }
                   })
               }
               console.log("모집글 생성 성공");
           },
           error: function(data) {
               var response = JSON.parse(data.responseText);
               alert(response.message);
               console.log(response.message);
           }
     })
}

// ---------------- 모집글 입장 시 ------------
let id;

function backdropDelete() {
    // $('.modal-backdrop').remove();
}

var myModal = new bootstrap.Modal(document.getElementById('enterRoomModal'), {
    backdropDelete: true
});

$(function () {
    //모달 창이 열릴 때 data-id로 적어둔 roomId와 nickname 갖고오기
    $("#enterRoomModal").on("show.bs.modal", function (event) {
        id = $(event.relatedTarget).data('id');
        nickname = $(event.relatedTarget).data('nickname');
    });
})


function chkRoomUserCnt(id, nickname) {
    let chk;

    $.ajax({
        type: "GET",
        url: "/mozip/chat/chkRoomUserCnt",
        data: {
            id: id,
            nickname: nickname
        },
        async: false,
        success: function (result) {
            if (!result) {
                alert("인원이 다 찼거나 참여한 방이 있습니다!");
            }
            chk = result;
        },
        error: function () {
            alert("실패!");
        }
    })
    return chk;
}


let enter_loading = document.querySelector(".loading-div");

function enterRoom() {
    enter_loading.style.display = "block";
    document.querySelector(".join-p").style.display = "none";
    if (chkRoomUserCnt(id, nickname)) {
        $.ajax({
            type: "GET",
            url: "/mozip/chat/room?id=" + id,
            data: {
            },
            async: false,
            success: function (result) {
                location.href = "/mozip/chat/room?id=" + id;
            },
            error: function () {
                alert("실패!");
            }
        })
    }
}




