package backend.codebackend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor //기본 생성자를 추가해주는 어노테이션
@Builder
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    private String Login;
    private String pw;
    private String pwcheck;
    private String nickname;
    private String pnum;

    private String certified;

    public Member(Long id, String Login, String pw, String pwcheck, String nickname, String pnum, String certified) {
        this.id = id;
        this.Login = Login;
        this.pw = pw;
        this.pwcheck = pwcheck;
        this.nickname = nickname;
        this.pnum = pnum;
        this.certified = certified;
    }

    public String getLoginId() {
        return Login;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

}
