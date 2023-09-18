package backend.codebackend.domain;

import jakarta.validation.constraints.NotEmpty;

public class SignInRequest {

    @NotEmpty(message = "유효하지 않은 로그인 형식입니다.")
    private String signid;
    @NotEmpty
    private String signpw;

    public SignInRequest(String signid, String signpw) {
        this.signid = signid;
        this.signpw = signpw;
    }

    public String getLogin() {
        return signid;
    }

    public void setLogin(String signid) {
        this.signid = signid;
    }


    public String getPw() {
        return signpw;
    }

    public void setPw(String signpw) {
        this.signpw = signpw;
    }
}
