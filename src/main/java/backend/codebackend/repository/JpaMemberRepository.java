package backend.codebackend.repository;

import backend.codebackend.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor //생성자 주입으로 생성자 생략
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em; //jpa를 라이브러리로 받으면 스프링 부트가 자동으로 EntityManager를 생성해줌


    @Override
    public Member save(Member member) {
        em.persist(member); //persist : 영구적으로 저장?
        return member;
    }

    @Override
    public Optional<Member> findById(String Login) {
        //Member member = em.find(Member.class, Login);
        //return Optional.ofNullable(member);

        List<Member> result = em.createQuery("select m from Member m where m.Login = :Login", Member.class)
                .setParameter("Login", Login)
                .getResultList();
        
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String nickname) {
        List<Member> result = em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return result.stream().findAny();
    }



    @Override
    public List<Member> findAll() {
        TypedQuery typedQuery = em.createQuery("select m from Member m", Member.class);
        return typedQuery.getResultList();
        /*
        return em.createQuery("select m from Member m", Member.class) //객체를 대상으로 쿼리를 날림. m이 sql로 번역됨
                .getResultList();

         */
    }

}
