package backend.codebackend.repository;

import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.dto.AccountDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JpaPaymentDetailsRepository implements PaymentDetailsRepository{
    private EntityManager em;

    @Override
    public List<PaymentDetails> findAll(Long id) {
        return null;
    }

    @Override
    public void save(PaymentDetails paymentDetails) {
        em.persist(paymentDetails);
    }

    @Override       // 송금자 계좌 정보 업데이트
    public void senderAccountInfo(Long paymentId, AccountDto senderAccountDto) {
        String updateQuery = "update PaymentDetails m " +
                             "set m.senderAccountDto = :senderAccountDto" +
                             "where m.payment_id = :payment_id";
        em.createQuery(updateQuery)
                .setParameter("senderAccountDto", senderAccountDto)
                .setParameter("paymentId", paymentId)
                .executeUpdate();
    }
    @Override       // 결제 여부 업데이트
    public void updateIsPaid(Long paymentId, int isPaid){
        String updateQuery = "update PaymentDetails m " +
                             "set m.is_paid = :is_paid" +
                             "where m.payment_id = :m.payment_id";
        em.createQuery(updateQuery)
                .setParameter("isPaid", isPaid)
                .setParameter("paymentId", paymentId)
                .executeUpdate();
    }
    @Override       // 결제 완료 여부 업데이트
    public void updateCompletePayment(Long paymentId, int completePayment) {
        String updateQuery = "UPDATE PaymentDetails m " +
                "SET m.complete_payment = :completePayment " +
                "WHERE m.payment_id = :paymentId";
        em.createQuery(updateQuery)
                .setParameter("completePayment", completePayment)
                .setParameter("paymentId", paymentId)
                .executeUpdate();
    }

}
