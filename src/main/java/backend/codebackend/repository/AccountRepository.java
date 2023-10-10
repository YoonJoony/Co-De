package backend.codebackend.repository;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(Long number);
    Optional<Account> findByAccountNumber(Long number);


    Optional<Member> findById(String id);
    Optional<Member> findByName(String name);



}
