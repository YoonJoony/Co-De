package backend.codebackend.repository;

import backend.codebackend.domain.Mozip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MozipRepository {
    Mozip save(Mozip mozip);
    Optional<Mozip> findById(Long ID);

    Optional<Mozip> findByName(String Title);

    List<Mozip> findAll();
}
