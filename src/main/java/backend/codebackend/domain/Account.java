package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb", indexes = {
        @Index(name = "idx_account_number", columnList = "number")
})

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
}
