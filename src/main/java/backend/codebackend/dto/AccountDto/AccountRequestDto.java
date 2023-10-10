package backend.codebackend.dto.AccountDto;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.Member;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull
public class AccountRequestDto {

    // 세이브
    public static class AccountSaveRequestDto {
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(Member member) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .id(member.getId())
                    .build();
        }
    }

    // 예금(넣음)
    public static class AccountDepositRequestDto {

        @Digits(integer = 4, fraction = 4)
        private Long number;
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "DEPOSIT")
        private String gubun; // DEPOSIT
        @NotEmpty
        @Pattern(regexp = "^[0-9]{11}")
        private String tel;
    }

    // 출금(뺌)
    public static class AccountWithdrawRequestDto {

        @Digits(integer = 4, fraction = 4)
        private Long number;
        @Digits(integer = 4, fraction = 4)
        private Long password;
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "WITHDRAW")
        private String gubun;
    }

    // 계좌이체
    public static class AccountTransferRequestDto {

        @Digits(integer = 4, fraction = 4)
        private Long withdrawNumber;
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "TRANSFER")
        private String gubun;
    }

}
