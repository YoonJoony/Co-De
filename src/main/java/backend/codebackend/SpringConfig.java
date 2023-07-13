package backend.codebackend;

import backend.codebackend.repository.JpaMemberRepository;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.MemoryMemberRepository;
import backend.codebackend.service.MemberService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//빈을 직접 찾아서 등록하는거라 따로 서비스나 저장소에서 @service... @repo.. 안해줘도 됨


@Configuration
public class SpringConfig {


    private final EntityManager em;

    public SpringConfig(EntityManager em) {

        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }
}
