package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mozipId", referencedColumnName = "id", nullable = false)
    Mozip mozipId; // 모집글 Id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mozipStore", referencedColumnName = "store", nullable = false)
    Mozip mozipStore; // 모집글에서 선택한 가게

    int deliveryFee; // 배달요금
    int minFee;      // 최소 주문금액
    @Column(nullable = true)
    int hostFee;     // 호스트 배달요금
    @Column(nullable = true)
    int personalFee; // 호스트 제외인당 배다료금
}
