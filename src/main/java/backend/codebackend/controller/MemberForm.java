package backend.codebackend.controller;

public class MemberForm {
    private String id;
    private String pw;
    private String pwcheck;
    private String nickname;
    private int pnum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
