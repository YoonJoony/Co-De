package backend.codebackend.repository;

import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.dto.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

}
