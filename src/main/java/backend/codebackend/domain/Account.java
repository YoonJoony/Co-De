package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb", indexes = {
        @Index(name = "idx_account_number", columnList = "number")
})
@Entity
@Builder
public class Account {

    @Id
    private Long id;
    @Column(nullable = false)
    private String account_name;       // 어디 은행
    @Column(unique = true, nullable = false, length = 4)
    private String number;        // 계좌번호
    @Column(nullable = false, length = 4)
    private String password;      // 계좌비번
    @Column(nullable = false)
    private Long balance;       // 잔액

    @Column(nullable = false)
    private String username;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime create_at;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime update_at;

    private int is_paid;          // 결제
    private int complete_payment; // 결제 완료(결제 후)

    public Account(Long id, String account_name, String number, String password, Long balance, String username, LocalDateTime create_at, LocalDateTime update_at, int is_paid, int complete_payment) {
        this.id = id;
        this.account_name = account_name;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.username = username;
        this.create_at = create_at;
        this.update_at = update_at;
        this.is_paid = is_paid;
        this.complete_payment = complete_payment;
    }
}
