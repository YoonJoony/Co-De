package backend.codebackend.dto;
import backend.codebackend.domain.PaymentDetails;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@Setter
@Getter
public class PaymentDetailsDto {
    private Long paymentId;          // 주문 ID
    private Long userId;             // 유저 ID
    private Long mozipId;            // 방 번호
    private String nickname;         // 사용자 닉네임
    private String orderList;        // 주문 목록
    private int totalPrice ;         // 총 결제금액
    private PaymentDetails.PaymentStatus payStatus; // 결제 상태
    private LocalDateTime createdAt; // 생성 시간
    private String deliveryAddress;  // 배송 주소
}
