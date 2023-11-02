package backend.codebackend.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class SystemAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long system_id;

    private String accountNumber;   // 시스템 계좌번호
    private Long balance;           // 시스템 잔고(잔액)

    public SystemAccount(Long system_id, String accountNumber, Long balance) {
        this.system_id = system_id;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
