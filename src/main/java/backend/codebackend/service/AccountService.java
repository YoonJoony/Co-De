package backend.codebackend.service;

import java.util.List;
import java.util.Optional;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.Member;
import backend.codebackend.dto.AccountDto.AccountResponseDto;
import backend.codebackend.repository.AccountRepository;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public boolean isNonghyupAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account != null){

            return isNonghyupAccount(account);  // 계좌가 존재 O
        }
        return false;   // 계좌가 존재 x
    }

    private boolean isNonghyup(Account account) {
        String accountNumber = account.getAccountNumber();

        // 농협(중앙회) 계좌 패턴
        if (accountNumber.startsWith("301") || accountNumber.startsWith("302") || accountNumber.startsWith("312")) {
            return true; // 농협(중앙회) 계좌
        }

        // 농협(단위농협) 계좌 패턴
        if (accountNumber.startsWith("351") || accountNumber.startsWith("352") || accountNumber.startsWith("356")) {
            return true; // 농협(단위농협) 계좌
        }
        return false;
    }
}
