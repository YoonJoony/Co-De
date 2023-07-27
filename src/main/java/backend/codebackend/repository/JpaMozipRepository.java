package backend.codebackend.repository;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JpaMozipRepository implements MozipRepository {

    private final EntityManager em; //jpa를 라이브러리로 받으면 스프링 부트가 자동으로 EntityManager를 생성해줌

    @Override
    public Mozip save(Mozip mozip) {
        return null;
    }

    @Override
    public List<Mozip> findAll() {
        return null;
    }
}
