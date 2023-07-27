package backend.codebackend.repository;

import backend.codebackend.domain.Mozip;

import java.util.List;

public interface MozipRepository {
    Mozip save(Mozip mozip);

    List<Mozip> findAll();

}
