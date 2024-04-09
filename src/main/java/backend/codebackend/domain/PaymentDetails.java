package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentDetails {
    public enum PaymentStatus{
        COMPLETED, // 결제 완료
        FAILED,    // 결제 실패
        CANCELED   // 결제 취소
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;          // 주문 ID

    // 한명의 사용자에 대해 하나의 결제 내역만 존재
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private Member member;

    // 한개의 모집글에 대해 여러개의 결제내역이 존재
    // nullable = ture 설정으로 채팅방이 삭제될 시 투플이 삭제되지 않고 null 로 표시
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mozipId", referencedColumnName = "id", nullable = true)
    private Mozip mozip;             // 방 번호

    private String nickname;         // 사용자 닉네임
    private String orderList;        // 주문 목록
    private int totalPrice ;         // 총 결제금액

    @Enumerated(EnumType.STRING)     // enum을 DB와 맵핑하고 싶을때 필수 작성.
    private PaymentStatus payStatus; // 결제 상태

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    private String deliveryAddress;  // 배송 주소

    @PrePersist // JPA가 엔티티를 데이터베이스 처음 저장할 때 호출
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}