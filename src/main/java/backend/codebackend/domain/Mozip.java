package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
public class Mozip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @Column(length = 20, nullable = false)
    private String title;
    @Column(length = 10, nullable = false)
    private Long distance_limit;

    private String categories;

    private int usercount; //채팅방 인원 수, 방 만들때마다 방장 인원도 추가해야 하므로 + 1
    private int peoples; //채팅방 최대 인원 제한

    //스프리에서 제공하는 시간관련 변수
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_Date; //생성시간

    private String nickname;

    public Mozip(Long id, String title, Long distance_limit, String categories, int usercount, int peoples, LocalDateTime create_Date, String nickname) {
        this.id = id;
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.usercount = usercount;
        this.peoples = peoples;
        this.create_Date = create_Date;
        this.nickname = nickname;
    }
}
