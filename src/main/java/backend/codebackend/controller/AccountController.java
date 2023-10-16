package backend.codebackend.controller;

import backend.codebackend.domain.Account;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.AccountRepository;
import backend.codebackend.service.AccountService;
import backend.codebackend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;
    private final MemberService memberService;

    //계좌추가
    @PostMapping("/account/save")
    public boolean accountRegister(String username, String number, Long password, String accountName, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        AccountDto accountDto = AccountDto.builder()
                .id(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getId())
                .number(number)
                .password(password)
                .balance(100000l)
                .username(username)
                .accountName(accountName)
                .build();

        if(accountService.save(accountDto))
            return true;

        return false;
    }

    //계좌조회
    @PostMapping("/account/login-user")
    public Account findAccount(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("세션이 없습니다");
            return null;
        }

        Account account = accountService.findAccount(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getId());

        return account;
    }

    //계좌삭제

    //송금

    //정산(방장한테)
}
