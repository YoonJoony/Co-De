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
        em.persist(mozip); //persist : 영구적으로 저장?
        return mozip;
    }

    @Override
    public Optional<Mozip> findById(Long id) {
        //Member member = em.find(Member.class, Login);
        //return Optional.ofNullable(member);

        List<Mozip> result = em.createQuery("select m from Mozip m where m.id = :id", Mozip.class)
                .setParameter("id", id)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Mozip> findByName(String title) {
        List<Mozip> result = em.createQuery("select m from mozip m where m.title = :title", Mozip.class)
                .setParameter("title", title)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Mozip> findAll() {
        TypedQuery typedQuery = em.createQuery("select m from Mozip m order by m.create_Date desc", Mozip.class); //테이블 생성시간 역순으로 조회
        return typedQuery.getResultList();
        /*
        return em.createQuery("select m from Member m", Member.class) //객체를 대상으로 쿼리를 날림. m이 sql로 번역됨
                .getResultList();

         */
    }
}
