package backend.codebackend.domain;


import backend.codebackend.controller.MemberForm;

public class Member {
    private String Id;
    private String pw;
    private String pwcheck;
    private String nickname;
    private int pnum;

    public Member(String Id, String pw, String pwcheck, String nickname, int pnum) {
        this.Id = Id;
        this.pw = pw;
        this.pwcheck = pwcheck;
        this.nickname = nickname;
        this.pnum = pnum;
    }

    public Member(Member.Builder builder) { //Builder에 변수를 다 저장하고 나서 build()메소드가 실행되면 이 생성자 실행되어서 변수들 다 저장됨
        this.Id = builder.Id; //builder 클래스의 id 저장.
        this.pw = builder.pw;
        this.pwcheck = builder.pwcheck;
        this.nickname = builder.nickname;
        this.pnum = builder.pnum;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
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

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public static class Builder {
        private String Id;
        private String pw;
        private String pwcheck;
        private String nickname;
        private int pnum;

        public Builder(String Id, String pw, String pwcheck) { //필수로 저장되어야 하는 객체
            this.Id = Id;
            this.pw = pw;
            this.pwcheck = pwcheck;
        }

        public Member.Builder nickname(String nickname) { //수정될 가능성 있는 객체만 따로 클래스로 빼서 저장
            this.nickname = nickname;
            return this;
        }

        public Member.Builder pnum(int pnum) {
            this.pnum = pnum;
            return this;
        }

        public Member build() {
            return new Member(this); //저장된 변수를 가지고 있는 Builder 클래스가 Member 생성자에 반환됨.
        }
    }
}
