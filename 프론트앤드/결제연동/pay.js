var IMP = window.IMP;
IMP.init("imp38136157");

// 결제 창 호출

function requestPay() {
  IMP.request_pay(
    {
      pg: "html5_inicis",
      pay_method: "vbank",
      merchant_uid: "ex" + new Date().getTime(), // 주문번호
      name: "뿌링클 치킨",
      amount: 64900, // 숫자 타입
      buyer_email: "gildong@gmail.com",
      buyer_name: "홍길동",
      buyer_tel: "010-4242-4242",
      buyer_addr: "서울특별시 강남구 신사동",
      buyer_postcode: "01181",
    },
    function (rsp) {
      console.log(rsp);
      if (rsp.success) {
        var msg = "결제가 완료되었습니다.";
        alert(msg);
      } else {
        var msg = "결제에 실패하였습니다.";
        msg += "에러내용 : " + rsp.error_msg;
        alert(msg);
      }
    }
    // callback
    //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다
  );
}
