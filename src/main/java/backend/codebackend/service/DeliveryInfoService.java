package backend.codebackend.service;

import backend.codebackend.domain.DeliveryInfo;
import backend.codebackend.domain.Mozip;
import backend.codebackend.repository.DeliveryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryInfoService {
    private final DeliveryInfoRepository deliveryInfoRepository;
    public void deliveryInfoSave(Mozip mozip, int deliveryFee, int minFee) {
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .mozipId(mozip)
                .mozipStore(mozip)
                .deliveryFee(deliveryFee)
                .minFee(minFee)
                .build();

        deliveryInfoRepository.save(deliveryInfo);
    }

    public DeliveryInfo deliveryInfoSelect(Long roomId) {
        return deliveryInfoRepository.findByMozipId_Id(roomId);
    }
}
