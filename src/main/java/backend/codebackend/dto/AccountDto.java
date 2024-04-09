package backend.codebackend.dto;

import backend.codebackend.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NotNull
@Builder
public class AccountDto {

    private Long id;
    private String number;        // 계좌번호
    private String password;      // 계좌비번
    private Long balance;       // 잔액

    private String username;

    private String account_name;       // 계좌 넘버
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;
}
