package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    //스프리에서 제공하는 시간관련 변수
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_Date; //생성시간

    @LastModifiedDate
    private LocalDateTime modified_Date; //수정시간

    private String nickname;

    @Builder
    public Mozip(Long id, String title, Long distance_limit, String categories, String peoples, String nickname) {
        this.id = id;
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.peoples = peoples;
        this.nickname = nickname;
    }
}
