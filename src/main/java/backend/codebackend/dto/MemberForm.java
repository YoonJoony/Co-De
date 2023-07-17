package backend.codebackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class MemberForm {

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String Login;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "(?=.*[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣])(?=.*[0-9])(?=.*[^\\w\\s]).{4,20}",
            message = "길이가 8~20의 알파벳, 숫자, 특수문자가 각 1개이상 포함된 어야 합니다.")
    private String pw;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String pwcheck;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "길이가 3~20의 알파벳, 숫자, 한글만 허용 됩니다.")
    private String nickname;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$",
            message = "01로 시작하는 10-11자리 숫자여야 합니다.")
    private String pnum;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String certified;

    public MemberForm(String Login, String pw, String pwcheck, String nickname, String pnum, String certified) {
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
}
