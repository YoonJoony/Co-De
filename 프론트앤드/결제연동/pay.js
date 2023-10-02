// 결제 창 호출
var IMP = window.IMP;
IMP.init("imp38136157");

function requestPay() {
  IMP.request_pay(
    {
      pg: "uplus",
      pay_method: "card",
      merchant_uid: "test_" + new Date().getTime(),
      name: "테스트 결제",
      amount: 100,
      buyer_tel: "010-0000-0000",
    },
    function (rsp) {
      if (rsp.success) {
        // 결제 성공 시: 결제 승인 또는 가상계좌 발급에 성공한 경우
        // jQuery로 HTTP 요청
        alert("결제 성공");

        jQuery
          .ajax({
            url: "{서버의 결제 정보를 받는 endpoint}",
            method: "POST",
            headers: { "Content-Type": "application/json" },
            data: {
              imp_uid: rsp.imp_uid, // 결제 고유번호
              merchant_uid: rsp.merchant_uid, // 주문번호
            },
          })
          .done(function (data) {
            // 가맹점 서버 결제 API 성공시 로직
          });
      } else {
        alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
      }
    }
  );
}
