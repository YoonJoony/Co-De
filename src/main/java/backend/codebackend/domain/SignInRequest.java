package backend.codebackend.domain;



public class SignInRequest {
    private String Login;
    private String pw;

    public SignInRequest(builder builder) {
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        Login = Login;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public static class builder{
        private String Login;
        private String pw;

        public builder(String Login, String pw){
            this.Login = Login;
            this.pw = pw;
        }

        public builder Login(String Login){
            this.Login = Login;
            return this;
        }
        public builder pw(String pw){
            this.pw = pw;
            return this;
        }
        public SignInRequest build() {
            return new SignInRequest(this);
        }
    }
}
