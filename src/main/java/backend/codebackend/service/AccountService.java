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
                .account_name(accountDto.getAccount_name())
                .build();

        if(!duplicateAccount(accountTb))
            return false;

        accountRepository.save(accountTb);
        return true;
    }

    //계좌 중복 여부 (농
    public boolean duplicateAccount(Account account) {
        if (accountRepository.findAccount(account.getId()).isPresent()) {
            System.out.println("추가한 계좌가 있습니다.");
            deleteAccount(account.getId()); //본인 계좌 삭제
            return true;
        }
        return true;
    }

    public Account findAccount(Long id) {
        Account findAccount = new Account();
        if (accountRepository.findAccount(id).isPresent()) {
            findAccount = accountRepository.findAccount(id).get();
            return findAccount;
        }
        findAccount.setAccount_name("계좌를 추가 해주세요");
        findAccount.setNumber("");
        findAccount.setUsername("");
        findAccount.setBalance(0l);

        return findAccount;
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
