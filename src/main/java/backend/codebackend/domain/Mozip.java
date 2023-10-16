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
    @Column(length = 10, nullable = false)
    private String categories;
    @Column(length = 20, nullable = false)
    private String store;

    private int usercount; //채팅방 인원 수, 방 만들때마다 방장 인원도 추가해야 하므로 + 1
    private int peoples; //채팅방 최대 인원 제한


//    //모집글 생성 시 호스트 지정: 모집글을 생성할 때, 모집글을 생성한 사용자를 호스트로 지정, 이 정보를 모집글 엔티티에 저장
//    //모집글을 생성한 사용자를 호스트로 지정
//    //@ManyToOne(다대일)로 쓰는 이유는 한명의 사용자(ChatUser)가 여러 개의 모집글(Mozip)을 생성할 수 있으며, 각 모집글은 해당 사용자를 참조함. 따라서 'Mozip'엔티티 -> 'ChatUser'엔티티 참조
//    //@Column(updatable = false)
//    @Transient
//    @ManyToOne  //다대일 (N : 1) 관계
//    private ChatUser host;


    //스프리에서 제공하는 시간관련 변수
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_Date; //생성시간

    private String nickname;

    public Mozip(Long id, String title, Long distance_limit, String categories, String store, int usercount, int peoples, LocalDateTime create_Date, String nickname) {
        this.id = id;
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.store = store;
        this.usercount = usercount;
        this.peoples = peoples;
        this.create_Date = create_Date;
        this.nickname = nickname;
    }
}
