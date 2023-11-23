package backend.codebackend.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor //기본 생성자를 추가해주는 어노테이션
@Builder
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @Column(length = 20, nullable = false)
    private String login;
    @Column(length = 20, nullable = false)
    private String pw;

    @Column(length = 20, nullable = false)
    private String pwcheck;

    @Column(length = 10, nullable = false)
    private String username;
    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String address;
    @Column(length = 15, nullable = false)
    private String pnum;

    @Column(length = 17, nullable = false)
    private String certified;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Basket> baskets;
//
//    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
//    private ChatUser chatUser;


    public Member(Long id, String login, String pw, String pwcheck, String username, String nickname, String address, String pnum, String certified) {
        this.id = id;
        this.login = login;
        this.pw = pw;
        this.pwcheck = pwcheck;
        this.username = username;
        this.nickname = nickname;
        this.address = address;
        this.pnum = pnum;
        this.certified = certified;
    }
}
