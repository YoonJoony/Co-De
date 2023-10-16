package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public boolean save(AccountDto accountDto) {
        Account accountTb = Account.builder()
                .id(accountDto.getId())
                .number(accountDto.getNumber())
                .password(bCryptPasswordEncoder.encode(accountDto.getPassword())) //비밀번호 암호화
                .username(accountDto.getUsername())
                .balance(accountDto.getBalance())
                .account_name(accountDto.getAccountName())
                .build();

        if(!duplicateAccount(accountTb))
            return false;

        accountRepository.save(accountTb);
        return true;
    }

    //계좌 중복 여부 (농
    public boolean duplicateAccount(Account accountTb) {
        if (accountRepository.findAccount(accountTb.getId()).isPresent())
            return false;

        return true;
    }

    public Account findAccount(Long id) {
        Account accountTb = accountRepository.findAccount(id).get();

        return accountTb;
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteAccount(id);
    }

    //비밀번호 확인
    public boolean checkAccountPassword(Long id, String password) {
        //현재 접속중인 사용자의 계좌 조회
        Account stiredAccountTb = findAccount(id);

        //비밀번호랑 암호화된 비밀번호랑 같으면 true
        return bCryptPasswordEncoder.matches(password, stiredAccountTb.getPassword());
    }
}
