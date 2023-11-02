package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.SystemAccount;
import backend.codebackend.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class PaymentsDetailsService {
    private final AccountService accountService;
    private final SystemAccountService systemAccountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean sendMoneyToSystemAccount(Long id, int price) {
        // 송금자 정보 가져오기
        Account senderAccount = accountService.findAccount(id);
        AccountDto senderAcountDto = AccountDto.builder()
                .id(senderAccount.getId())
                .number(senderAccount.getNumber())
                .password(bCryptPasswordEncoder.encode(senderAccount.getPassword())) //비밀번호 암호화
                .username(senderAccount.getUsername())
                .balance(senderAccount.getBalance())
                .account_name(senderAccount.getAccount_name())
                .build();

        // 시스템 계좌 정보 가져오기
        SystemAccount systemAccount = systemAccountService.getSystemAccountInfo();

        // 송금자의 계좌에서 돈 감소
        if (senderAcountDto.getBalance() >= price){
            senderAcountDto.setBalance(senderAcountDto.getBalance() - price);
            accountService.save(senderAcountDto);

            // 시스템 계좌에 돈 추가
            systemAccount.setBalance(systemAccount.getBalance() + price);
            systemAccountService.updateSystemAccount(systemAccount);

            return true;
        }
        return false;
    }


}
