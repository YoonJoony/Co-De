package backend.codebackend.controller;

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

    @PostMapping("")
    public String accountRegister(AccountDto accountDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        accountService.save(accountDto, memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getId());

        return "redirect:/";
    }
}
