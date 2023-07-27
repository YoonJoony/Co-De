package backend.codebackend.service;

import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.repository.MozipRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MozipService {
    private MozipRepository mozipRepository;

    public MozipService(MozipRepository mozipRepository) {
        this.mozipRepository = mozipRepository;
    }

    @Transactional
    public Long savePost(MozipForm mozipForm) {
        return mozipRepository.save(mozipForm.toEntity()).getTitle();
    }

    @Transactional
    public List<MozipForm> getMozipList(){
        List<Mozip> mozipList = mozipRepository.findAll();
        List<MozipForm> mozipFormList = new ArrayList<>();

        for(Mozip mozip : mozipList){
            MozipForm mozipForm = MozipForm.builder()
                    .id(mozip.getId())
                    .title(mozip.getTitle())
                    .distanceLimit(mozip.getDistanceLimit())
                    .category(mozip.getCategory())
                    .numberOfPeople(mozip.getNumberOfPeople())
                    .build();
            mozipFormList.add(mozipForm);
        }
        return mozipFormList;
    }

}
