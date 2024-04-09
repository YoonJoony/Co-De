package backend.codebackend.domain;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
