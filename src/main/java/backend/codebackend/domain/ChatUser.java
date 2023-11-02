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

//    //일대다 매핑, mappedBy속성은 관계의 주인인 Mozip엔티티의 host필드를 참조
//    // 이렇게 되면, ChatUser 엔티티는 여러 모집글을 호스팅할 수 있으며, Mozip 엔티티의 host필드와 연결됨
//    //@Column(updatable = false)
//    @Transient
//    @OneToMany(mappedBy = "host")   // "host"는 Mozip 엔티티의 host 필드 이름과 일치해야함
//    private List<String> hostedMozips;   //hostedMozip;은 이 필드가 List 타입의 Mozip엔티티 객체들을 담고 있다는 것을 의미

    public ChatUser(Long id, String nickname, int host) {
        this.id = id;
        this.nickname = nickname;
        this.host = host;
        //this.hostedMozips = hostedMozips;
    }
}
