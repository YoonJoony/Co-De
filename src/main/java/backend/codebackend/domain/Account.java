package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb", indexes = {
        @Index(name = "idx_account_number", columnList = "number")
})
@Entity
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 4)
    private Long number;        // 계좌번호
    @Column(nullable = false, length = 4)
    private Long password;      // 계좌비번
    @Column(nullable = false)
    private Long balance;       // 잔액

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String accountNumber;       // 계좌 넘버
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

    public Account(Long id, Long number, Long password, Long balance, String username, String accountNumber, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.username = username;
        this.accountNumber = accountNumber;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
