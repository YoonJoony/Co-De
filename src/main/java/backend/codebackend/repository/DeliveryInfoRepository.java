package backend.codebackend.repository;

import backend.codebackend.domain.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Long> {
    DeliveryInfo findByMozipId_Id(Long mozipId);
}
