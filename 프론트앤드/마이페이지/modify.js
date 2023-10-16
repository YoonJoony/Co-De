// 계좌 변경 버튼 클릭 시 수정 form 보이게 함
function modify_account_btn() {
  const box = document.getElementById("modify_account_form");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}

// 비밀번호 변경 버튼 클릭 시 수정 form 보이게 함
function modify_pw_btn() {
  const box = document.getElementById("modify_password_form");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}

// 닉네임 변경 버튼 클릭 시 수정 form 보이게 함
function modify_nickname_btn() {
  const box = document.getElementById("modify_nickname_form");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}

// 프로필 사진 변경 버튼 클릭 시 수정 form 보이게 함
function modify_profile_image_btn() {
  const box = document.getElementById("modify_profile_image_form");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}

// 주소 변경 버튼 클릭 시 수정 form 보이게 함
function modify_address_btn() {
  const box = document.getElementById("modify_address_form");

  // btn1 숨기기 (display: none)
  if (box.style.display !== "block") {
    box.style.display = "block";
  }
  // btn` 보이기 (display: block)
  else {
    box.style.display = "none";
  }
}
// 다음 주소
window.onload = function(){
  document.getElementById("find_address").addEventListener("click", function(){ //주소입력칸을 클릭하면
      new daum.Postcode({
          oncomplete: function(data) { //선택시 입력값 세팅
              document.getElementById("modified_ad").value = data.address; // 주소 넣기
              document.querySelector("#modified_ad_detail").focus(); //상세입력 포커싱
          }
      }).open();
  });
}
// $(function(){
//   document.getElementById("find_address").addEventListener("click", function(){ //주소입력칸을 클릭하면
//       //카카오 지도 발생
//       new daum.Postcode({
//           oncomplete: function(data) { //선택시 입력값 세팅
//               document.getElementById("modified_ad").value = data.address; // 주소 넣기
//               document.querySelector("modified_ad_detail").focus(); //상세입력 포커싱
//           }
//       }).open();
//   });
// });