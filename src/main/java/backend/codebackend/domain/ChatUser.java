package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


//채팅방 id에 해당하는 유저를 저장하는 테이블
@Getter
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Builder
public class ChatUser {

    @Id
    private Long id;

    @Column(length = 10, nullable = false)
    private String nickname;
    private int host;    // 방장

//    @ManyToOne
//    @JoinColumn(name = "member_id") // 연관 관계의 주인인 Member 엔티티의 필드명을 지정
//    private Member member; // Member 엔티티와의 연관 관계를 설정하는 필드 추가

    public ChatUser(Long id, String nickname, int host) {
        this.id = id;
        this.nickname = nickname;
        this.host = host;
    }
}
