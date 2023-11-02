package backend.codebackend.repository;

import backend.codebackend.domain.SystemAccount;

import java.util.List;

public interface SystemAccountRepository {
    SystemAccount getSystemAccountInfo();

    boolean sendMoneyToHost(Long amount);
}
