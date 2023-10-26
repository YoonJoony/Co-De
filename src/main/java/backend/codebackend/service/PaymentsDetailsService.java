package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.SystemAccount;
import backend.codebackend.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class PaymentsDetailsService {
    private final AccountService accountService;
    private final SystemAccountService systemAccountService;
    private final TransactionRecordRepository transactionRecordRepository;

    public boolean sendMoneyToSystemAccount(Long senderUserId, Long amount) {
        // 송금자 정보 가져오기
        Account senderAccount = accountService.findAccount(senderUserId);

        // 시스템 계좌 정보 가져오기
        SystemAccount systemAccount =systemAccountService.
    }


}
