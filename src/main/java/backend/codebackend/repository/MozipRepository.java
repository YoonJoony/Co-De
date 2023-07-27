package backend.codebackend.repository;

import backend.codebackend.domain.Mozip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MozipRepository extends JpaRepository<Mozip, Long> {
    Mozip save(Mozip mozip);

}
