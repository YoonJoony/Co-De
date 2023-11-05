package backend.codebackend.repository;

import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.dto.AccountDto;

import java.util.List;

public interface PaymentDetailsRepository {

    List<PaymentDetails> findAll(Long id);
    void save(PaymentDetails paymentDetails);

    void senderAccountInfo(Long paymentId, AccountDto senderAccountDto);

    void updateIsPaid(Long paymentId, int isPaid);

    void updateCompletePayment(Long paymentId, int completePayment);


}
