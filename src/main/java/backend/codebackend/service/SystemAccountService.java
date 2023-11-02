package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.SystemAccount;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.ChatUserRepository;
import backend.codebackend.repository.SystemAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


@Transactional
@RequiredArgsConstructor
public class SystemAccountService {
    private final EntityManager em;
    private final SystemAccountRepository systemAccountRepository;
    private final ChatUserRepository chatUserRepository;
    private final AccountService accountService;
    private final SystemAccountService systemAccountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 시스템 계좌 정보 조회
    public SystemAccount getSystemAccountInfo() {
        return systemAccountRepository.getSystemAccountInfo();
    }

    //송금 처리(시스템 계좌 -> 방장(호스트)에게 모인 돈 송금
    public boolean sendMoneyToHost(Long id, Long price) {
        SystemAccount systemAccount = getSystemAccountInfo();
        if (systemAccount.getBalance() >= price) {
            //방장 계좌 찾기
            Long hostAccountId = chatUserRepository.findHost(id, nickname);

            if (hostAccountId != null) {
                // 방장에게 돈 송금
                Account hostAccount = accountService.findAccount(hostAccountId);
                AccountDto hostAccountDto = AccountDto.builder()
                        .id(hostAccount.getId())
                        .number(hostAccount.getNumber())
                        .password(bCryptPasswordEncoder.encode(hostAccount.getPassword())) //비밀번호 암호화
                        .username(hostAccount.getUsername())
                        .balance(hostAccount.getBalance())
                        .account_name(hostAccount.getAccount_name())
                        .build();
                hostAccount.setBalance(hostAccount.getBalance() + price);
                accountService.save(hostAccountDto);

                //시스템 계좌에서 돈 빼기
                systemAccount.setBalance(systemAccount.getBalance() - price);
                systemAccountService.updateSystemAccount(systemAccount);

                return true;
            }
        }
        return false;
    }
    // 시스템 계좌 잔액 업데이트
    public void updateSystemAccount(SystemAccount systemAccount) {
        em.merge(systemAccount);
    }


}
