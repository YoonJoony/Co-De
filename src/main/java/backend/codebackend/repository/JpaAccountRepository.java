package backend.codebackend.repository;

import backend.codebackend.domain.Account;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaAccountRepository implements AccountRepository{

    private final EntityManager em;

    @Override
    public Account save(Account account) {
//        em.persist(account);
        return null;
    }

    @Override
    public Optional<Account> findAccount(String username, String nickname) {
        List<Account> result = em.createQuery("select m from Account m where m.username = :username and m.nickname = :nickname", Account.class)
                .setParameter("username", username)
                .setParameter("nickname", nickname)
                .getResultList();

        return result.stream().findAny();
    }
}
