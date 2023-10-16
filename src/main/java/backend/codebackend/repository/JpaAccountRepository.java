package backend.codebackend.repository;

import backend.codebackend.domain.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaAccountRepository implements AccountRepository{

    private final EntityManager em;

    @Override
    public void save(Account account) {
        em.persist(account);
    }

    @Override
    public Optional<Account> findAccount(Long id) {
        List<Account> result = em.createQuery("select m from Account m where m.id = :id", Account.class)
                .setParameter("id", id)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public void deleteAccount(Long id) {
        Query query = em.createQuery("delete from Account m where m.id = :id", Account.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
