package backend.codebackend.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //송금 거래 ID(각 송금거래를 고유하게 식별해줌)

    private Long senderAccountId;       // 송금자 id
    private Long receiveAccountId;      // 수신자 id(시스템 계좌)
    private Long amount;                // 송금 금액
    private String nickname;            // 닉네임

    public TransactionRecord(Long id, Long senderAccountId, Long receiveAccountId, Long amount, String nickname) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.receiveAccountId = receiveAccountId;
        this.amount = amount;
        this.nickname = nickname;
    }
}
