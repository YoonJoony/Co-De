package backend.codebackend.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;            //주문 ID

    private Long room_id;        // 방 번호
    private String nickname;     // 사용자 ID
    private String productName;  // 항목 이름
    private int price;           // 가격
    private int quantity;        // 수량
    private List<String> orderList;   // 주문 목록
    private int is_paid;          // 결제
    private int complete_payment; // 결제 완료(결제 후)

    public PaymentDetails(Long payment_id, Long room_id, String nickname, String productName, int price, int quantity, List<String> orderList, int is_paid, int complete_payment) {
        this.payment_id = payment_id;
        this.room_id = room_id;
        this.nickname = nickname;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.orderList = orderList;
        this.is_paid = is_paid;
        this.complete_payment = complete_payment;
    }
}