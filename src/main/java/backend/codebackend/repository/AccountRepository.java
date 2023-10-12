package backend.codebackend.repository;

import backend.codebackend.domain.Account;
import backend.codebackend.dto.AccountDto;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findAccount(Long id);
    void deleteAccount(Long id);
}
