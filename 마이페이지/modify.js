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
