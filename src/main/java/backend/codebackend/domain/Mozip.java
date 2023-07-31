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
@Builder
public class Mozip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @Column(length = 20, nullable = false)
    private String title;
    @Column(length = 10, nullable = false)
    private Long distance_limit;

    private String categories;
    private String peoples;

    public Mozip(Long id, String title, Long distance_limit, String categories, String peoples) {
        this.id = id;
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.peoples = peoples;
    }
}
