package backend.codebackend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    private String Login;
    private String pw;
    private String pwcheck;
    private String nickname;
    private String pnum;

    private String certified;

    public Member() {
        super();
    }

    public Member(Long id, String Login, String pw, String pwcheck, String nickname, String pnum, String certified) {
        this.id = id;
        this.Login = Login;
        this.pw = pw;
        this.pwcheck = pwcheck;
        this.nickname = nickname;
        this.pnum = pnum;
        this.certified = certified;
    }



    public Member(Member.Builder builder) { //Builder에 변수를 다 저장하고 나서 build()메소드가 실행되면 이 생성자 실행되어서 변수들 다 저장됨
        this.Login = builder.Login; //builder 클래스의 id 저장.
        this.pw = builder.pw;
        this.pwcheck = builder.pwcheck;
        this.nickname = builder.nickname;
        this.pnum = builder.pnum;
        this.certified = builder.certified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return Login;
    }

    public void setLoginId(String Login) {
        this.Login = Login;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPwcheck() {
        return pwcheck;
    }

    public void setPwcheck(String pwcheck) {
        this.pwcheck = pwcheck;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getCertified() {
        return certified;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }


    public static class Builder {
        private String Login;
        private String pw;
        private String pwcheck;
        private String nickname;
        private String pnum;
        private String certified;

        public Builder(String Login, String pw, String pwcheck) { //필수로 저장되어야 하는 객체
            this.Login = Login;
            this.pw = pw;
            this.pwcheck = pwcheck;
        }

        public Member.Builder nickname(String nickname) { //수정될 가능성 있는 객체만 따로 클래스로 빼서 저장
            this.nickname = nickname;
            return this;
        }

        public Member.Builder pnum(String pnum) {
            this.pnum = pnum;
            return this;
        }

        public Member.Builder certified(String certified) {
            this.certified = certified;
            return this;
        }

        public Member build() {
            return new Member(this); //저장된 변수를 가지고 있는 Builder 클래스가 Member 생성자에 반환됨.
        }
    }
}
