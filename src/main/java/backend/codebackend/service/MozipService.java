package backend.codebackend.service;

import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.repository.MozipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class MozipService {
    private final MozipRepository mozipRepository; //final을 해줘야 config에서 생성자 주입이 됨.

    public Mozip savePost(MozipForm mozipForm) {
        Mozip mozip = mozipForm.toEntity();
        mozipRepository.save(mozip);

        return mozip;
    }
}
