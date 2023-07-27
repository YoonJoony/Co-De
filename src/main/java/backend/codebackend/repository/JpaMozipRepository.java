package backend.codebackend.repository;

import backend.codebackend.domain.Mozip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JpaMozipRepository implements MozipRepository{

    private final EntityManager em;

    @Override
    public Mozip save(Mozip mozip) {
        em.persist(mozip);
        return mozip;
    }

    @Override
    public List<Mozip> findAll() {
        TypedQuery typedQuery = em.createQuery("select m from Member m", Mozip.class);
        return typedQuery.getResultList();
        /*
        return em.createQuery("select m from Member m", Member.class) //객체를 대상으로 쿼리를 날림. m이 sql로 번역됨
                .getResultList();

         */
    }
}
