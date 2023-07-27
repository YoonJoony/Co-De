package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  // JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알림.
@Builder
public class Mozip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @Column(length = 20, nullable = false)
    private String title;
    @Column(length = 10, nullable = false)
    private String distanceLimit;

    private String category;
    private String people;

    public Mozip(Long id, String title, String distanceLimit, String category, String people) {
        this.id = id;
        this.title = title;
        this.distanceLimit = distanceLimit;
        this.category = category;
        this.people = people;
    }
}
