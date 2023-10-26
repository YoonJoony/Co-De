package backend.codebackend.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //주문 ID

    private String nickname;     // 사용자 ID
    private String productName;  // 항목 이름
    private int price;           // 가격
    private int quantity;        // 수량
    private List<String> orderList; // 주문 목록
    private PaymentStatus paymentStatus; // 결제 상태
    private boolean paymentConfirmed; // 송금 확인 여부 (true 또는 false)

    public PaymentDetails(Long id, String nickname, String productName, int price,
                          int quantity, List<String> orderList, PaymentStatus paymentStatus, boolean paymentConfirmed) {
        this.id = id;
        this.nickname = nickname;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.orderList = orderList;
        this.paymentStatus = paymentStatus;
        this.paymentConfirmed = paymentConfirmed;
    }
}