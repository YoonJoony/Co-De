package backend.codebackend.service;


import backend.codebackend.domain.Account;
import backend.codebackend.dto.TransactionResponseDto;
import backend.codebackend.repository.AccountRepository;
import backend.codebackend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


}
