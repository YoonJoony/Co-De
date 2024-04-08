package backend.codebackend.service;
import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.dto.PaymentDetailsDto;
import backend.codebackend.repository.PaymentDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 빈을 직접 추가해주지 않았기 때문에 서비스로 빈 추가
@Transactional
@RequiredArgsConstructor
public class PaymentsDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    // 결제 내역 저장
    public PaymentDetailsDto savePayment(PaymentDetailsDto paymentDetailsDto, Member member, Mozip mozip){
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .member(member)
                .mozip(mozip)
                .nickname(paymentDetailsDto.getNickname())
                .orderList(paymentDetailsDto.getOrderList())
                .totalPrice(paymentDetailsDto.getTotalPrice())
                .payStatus(paymentDetailsDto.getPayStatus())
                .deliveryAddress(paymentDetailsDto.getDeliveryAddress())
                .build();

        paymentDetailsRepository.save(paymentDetails);
        return convertToDTO(paymentDetails);
    }
    
    // Entity 타입을 DTO 타입으로 변환
    public PaymentDetailsDto convertToDTO(PaymentDetails paymentDetails){
        return PaymentDetailsDto.builder()
                .paymentId(paymentDetails.getPaymentId())
                .userId(paymentDetails.getMember().getId())
                .mozipId(paymentDetails.getMozip().getId())
                .nickname(paymentDetails.getNickname())
                .orderList(paymentDetails.getOrderList())
                .totalPrice(paymentDetails.getTotalPrice())
                .payStatus(paymentDetails.getPayStatus())
                .createdAt(paymentDetails.getCreatedAt())
                .deliveryAddress(paymentDetails.getDeliveryAddress())
                .build();
    }
}
