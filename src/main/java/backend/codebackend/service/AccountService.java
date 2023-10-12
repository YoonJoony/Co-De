package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public boolean save(AccountDto accountDto) {
        Account account = Account.builder()
                .number(accountDto.getNumber())
                .password(accountDto.getPassword())
                .username(accountDto.getUsername())
                .nickname(accountDto.getNickname())
                .balance(accountDto.getBalance())
                .accountNumber(accountDto.getAccountNumber())
                .build();

        if(!duplicateAccount(account))
            return false;

        return true;
    }

    //계좌 중복 여부 (농
    public boolean duplicateAccount(Account account) {
        if (accountRepository.findAccount(account.getUsername(), account.getNickname()).get() == null)
            return false;

        return true;
    }

    public Account findAccount(String username, String nickname) {
        Account account = accountRepository.findAccount(username, nickname).get();

        return account;
    }
}
