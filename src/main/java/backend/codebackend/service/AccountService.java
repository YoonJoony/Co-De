package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public boolean save(AccountDto accountDto) {
        Account account = Account.builder()
                .id(accountDto.getId())
                .number(accountDto.getNumber())
                .password(accountDto.getPassword())
                .username(accountDto.getUsername())
                .balance(accountDto.getBalance())
                .accountName(accountDto.getAccountName())
                .build();

        if(!duplicateAccount(account))
            return false;

        return true;
    }

    //계좌 중복 여부 (농
    public boolean duplicateAccount(Account account) {
        if (accountRepository.findAccount(account.getId()).get() == null)
            return false;

        return true;
    }

    public Account findAccount(Long id) {
        Account account = accountRepository.findAccount(id).get();

        return account;
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteAccount(id);
    }
}
