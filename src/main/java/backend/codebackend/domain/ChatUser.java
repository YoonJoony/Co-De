package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


//채팅방 id에 해당하는 유저를 저장하는 테이블
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatUser {
    @Id
    private Long id;

    @Column(length = 10, nullable = false)
    private String nickname;
    private int host;    // 방장
}
