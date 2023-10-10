package backend.codebackend.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
    private String nickname;

    @Column(length = 100, nullable = false)
    private String address;
    @Column(length = 15, nullable = false)
    private String pnum;

    @Column(length = 17, nullable = false)
    private String certified;

    //데이터베이스에서 데이터의 생성 및 수정 시간을 추적하기 위해서는 createAt와 updateAt과 같은 필드가 필요
    @CreatedDate    // Insert
    @Column(nullable = false)
    private LocalDateTime createAt;
    @LastModifiedDate // Inser, Update
    @Column(nullable = false)
    private LocalDateTime updateAt;

    public Member(Long id, String login, String pw, String pwcheck, String nickname, String address, String pnum, String certified,
                  LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.login = login;
        this.pw = pw;
        this.pwcheck = pwcheck;
        this.nickname = nickname;
        this.address = address;
        this.pnum = pnum;
        this.certified = certified;
        this.createAt = createAt;
        this.updateAt = updateAt;

    }
}
