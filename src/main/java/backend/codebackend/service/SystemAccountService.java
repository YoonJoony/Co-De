package backend.codebackend.service;

import backend.codebackend.domain.SystemAccount;
import backend.codebackend.domain.TransactionRecord;
import backend.codebackend.repository.SystemAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Transactional
@RequiredArgsConstructor
public class SystemAccountService {
    private final SystemAccountRepository systemAccountRepository;

    // 시스템 계좌 정보 조회
    public SystemAccount getSystemAccountInfo() {
        return systemAccountRepository.getSystemAccountInfo();
    }
    // (사용자)송금 기록 조회
    public List<TransactionRecord> getTransactionHistory() {
        return systemAccountRepository.getTransactionHistory();
    }
    //송금 처리(시스템 계좌 -> 방장(호스트)에게 모인 돈 송금
    public boolean processTransferToHost(Long amount) {
        SystemAccount systemAccount = getSystemAccountInfo();

    }





}
