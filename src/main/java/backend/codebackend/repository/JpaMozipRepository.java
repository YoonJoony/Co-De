package backend.codebackend.repository;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaMozipRepository implements MozipRepository {

    private final EntityManager em; //jpa를 라이브러리로 받으면 스프링 부트가 자동으로 EntityManager를 생성해줌

    @Override
    public Mozip save(Mozip mozip) {
        em.persist(mozip);
        return mozip;
    }
    @Override
    public Optional<Mozip> findById(String Login) {

        List<Mozip> result = em.createQuery("select m from Mozip m where m.Login = :Login", Mozip.class)
                .setParameter("Login", Login)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Mozip> findByName(String nickname) {
        List<Mozip> result = em.createQuery("select m from Mozip m where m.nickname = :nickname", Mozip.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Mozip> findAll() {
        TypedQuery typedQuery = em.createQuery("select m from Mozip m", Mozip.class);
        return typedQuery.getResultList();
        /*
        return em.createQuery("select m from Member m", Member.class) //객체를 대상으로 쿼리를 날림. m이 sql로 번역됨
                .getResultList();

         */
    }
}
