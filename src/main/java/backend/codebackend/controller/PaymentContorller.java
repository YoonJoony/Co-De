package backend.codebackend.controller;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.dto.PaymentDetailsDto;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.MozipService;
import backend.codebackend.service.PaymentsDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class PaymentContorller {
    private final PaymentsDetailsService paymentsDetailsService;
    private final MemberService memberService;
    private final MozipService mozipService;
    @PostMapping("payment/save")
    public ResponseEntity<?> payemntDetailsSave(HttpSession session, Long mozipId, String menu, int totalPrice, boolean payStatus){

        Optional<Member> member = memberService.findLoginId(String.valueOf(session.getAttribute("memberId")));
        Optional<Mozip> mozip = mozipService.findRoomById(mozipId);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body("유저가 조회되지 않습니다.");
        }
        if (mozip.isEmpty()) {
            return ResponseEntity.badRequest().body("모집글이 조회되지 않습니다");
        }

        PaymentDetailsDto paymentDetailsDto = PaymentDetailsDto.builder()
                .nickname(member.get().getNickname())
                .orderList(menu)
                .totalPrice(totalPrice)
                .deliveryAddress(member.get().getAddress())
                .build();

        if (payStatus) {
            paymentDetailsDto.setPayStatus(PaymentDetails.PaymentStatus.COMPLETED);
        }
        else {
            paymentDetailsDto.setPayStatus(PaymentDetails.PaymentStatus.FAILED);
        }
        paymentsDetailsService.savePayment(paymentDetailsDto, member.get(), mozip.get());

        return ResponseEntity.ok("결제내역이 성공적으로 저장되었습니다.");
    }
}
