package backend.codebackend.repository;

import backend.codebackend.domain.SystemAccount;
import backend.codebackend.domain.TransactionRecord;

import java.util.List;

public interface SystemAccountRepository {
    SystemAccount getSystemAccountInfo();

    List<TransactionRecord> getTransactionHistory();

    boolean processTransferToHost(Long amount);
}
