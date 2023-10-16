package backend.codebackend.repository;

import backend.codebackend.domain.Account;

import java.util.Optional;

public interface AccountRepository {
    void save(Account accountTb);
    Optional<Account> findAccount(Long id);
    void deleteAccount(Long id);
}
