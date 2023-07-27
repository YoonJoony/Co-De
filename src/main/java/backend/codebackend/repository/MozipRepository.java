package backend.codebackend.repository;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MozipRepository {
    Mozip save(Mozip mozip);
    List<Mozip> findAll();
}
