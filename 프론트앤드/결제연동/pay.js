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
      console.log(rsp);
      // 결제검증
      $.ajax({
        type: "POST",
        url: "/verifyIamport/" + rsp.imp_uid,
      }).done(function (data) {
        console.log(data);

        // 위의 rsp.paid_amount 와 data.response.amount를 비교한후 로직 실행 (import 서버검증)
        if (rsp.paid_amount == data.response.amount) {
          alert("결제 및 결제검증완료");
        } else {
          alert("결제 실패");
        }
      });
    }
  );
}
