package backend.codebackend.repository;

import backend.codebackend.domain.Mozip;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MozipRepository {
    Mozip save(Mozip mozip);
    Optional<Mozip> findById(Long id);

    Optional<Mozip> findByName(String Title);

    List<Mozip> findAll();

    void plusUserCnt(Long id);

    void minusUserCnt(Long id);
    boolean mozipStatus(Long id);
    void updateMozipStatus(Long id);
}
