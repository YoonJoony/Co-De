package backend.codebackend.controller;

import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.PaymentsDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentContorller {
    private final PaymentsDetailsService paymentsDetailsService;
    private final MemberService memberService;

    @PostMapping("/paymentDetails")
    public List<PaymentDetails> listPaymentDetailsItems(@PathVariable Long chatroom_id) {
        List<PaymentDetails> paymentDetailsItems = paymentsDetailsService.findAll(chatroom_id);
        return paymentDetailsItems;
    }

    // 사용자 결제 내역 조회
    @GetMapping("/chat/paymentDetails")
    @ResponseBody
    public List<PaymentDetails> paymentDetails(Long payment_id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        List<PaymentDetails> paymentDetails = paymentsDetailsService.findAll(payment_id);

        if (paymentDetails == null) {
            log.info("결제내역 조회가 되지 않음. ");
        }
        return paymentDetails;
    }
}
