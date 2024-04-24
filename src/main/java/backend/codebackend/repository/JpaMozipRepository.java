package backend.codebackend.repository;

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
    public Optional<Mozip> findByName(String nickname) {
        List<Mozip> result = em.createQuery("select m from Mozip m where m.nickname = :nickname", Mozip.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Mozip> findAll() {
        TypedQuery<Mozip> typedQuery = em.createQuery("select m from Mozip m order by m.create_Date desc", Mozip.class); //테이블 생성시간 역순으로 조회
        return typedQuery.getResultList();
        /*
        return em.createQuery("select m from Member m", Member.class) //객체를 대상으로 쿼리를 날림. m이 sql로 번역됨
                .getResultList();

         */
    }

    @Override
    public void plusUserCnt(Long id) {
        List<Mozip> result = em.createQuery("select m from Mozip m where m.id = :id", Mozip.class)
                .setParameter("id", id)
                .getResultList();
        if(!result.isEmpty()) { //id에 해당하는 방이 있을 경우 입장 시 인원 + 1
            Mozip mozip = result.get(0);
            mozip.setUsercount(mozip.getUsercount()+1);
            em.merge(mozip);
        }
    }

    @Override
    public void minusUserCnt(Long id) {
        List<Mozip> result = em.createQuery("select m from Mozip m where m.id = :id", Mozip.class)
                .setParameter("id", id)
                .getResultList();
        if(!result.isEmpty()) { //id에 해당하는 방이 있을 경우 입장 시 인원 - 1
            Mozip mozip = result.get(0);
            mozip.setUsercount(mozip.getUsercount()-1);
            em.merge(mozip);
        }
    }

    //모집글 정산 상태 확인
    @Override
    public boolean mozipStatus(Long id) {
        try {
            String jpql = "SELECT m FROM Mozip m WHERE m.id = :id";
            Mozip query = em.createQuery(jpql, Mozip.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return query.getStatus() != Mozip.mozipStatus.정산전;
        } finally {
            em.close();
        }
    }

    //모집글 정산 상태 업데이트 (정산시작)
    @Override
    public void calculateStartStatus(Long id) {
        String minusItem = "update Mozip m set m.status = :status where m.id = :id ";

        em.createQuery(minusItem)
                .setParameter("id",  id)
                .setParameter("status", Mozip.mozipStatus.정산시작)
                .executeUpdate();   //executeUpdate 메서드는 업데이트된 엔티티 수를 반환
    }

    //모집글 정산 상태 업데이트 (정산전)
    @Override
    public void preCalculateStartStatus(Long id) {
        String minusItem = "update Mozip m set m.status = :status where m.id = :id ";

        em.createQuery(minusItem)
                .setParameter("id",  id)
                .setParameter("status", Mozip.mozipStatus.정산전)
                .executeUpdate();   //executeUpdate 메서드는 업데이트된 엔티티 수를 반환
    }
    // 모집글 삭제 메소드
    @Override
    public void deleteMozip(Long id) {
        Mozip mozip = em.find(Mozip.class, id); // id를 가진 Mozip 엔티티를 찾는다.
        // 엔티티가 존재하면 삭제한다.
        em.remove(mozip);
    }
    // 채팅방 내부 유저들 삭제
    @Override
    public void deleteChatUsers(Long chatId) {
        // ChatUser 테이블의 id 값이 chatId인 모든 튜플 삭제
        String jpql = "DELETE FROM ChatUser c WHERE c.id = :chatId";
        em.createQuery(jpql)
                .setParameter("chatId", chatId)
                .executeUpdate();
    }
}
