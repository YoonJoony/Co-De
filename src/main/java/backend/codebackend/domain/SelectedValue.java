package backend.codebackend.domain;

//카테고리 및 인원에 저장된 객체를 한개만 생성될 수 있도록 싱글톤으로 생성
public class SelectedValue {
    private static SelectedValue instance = new SelectedValue();

    public String getSelectedValue1() {
        return selectedValue1;
    }

    public void setSelectedValue1(String selectedValue1) {
        this.selectedValue1 = selectedValue1;
    }

    public String getSelectedValue2() {
        return selectedValue2;
    }

    public void setSelectedValue2(String selectedValue2) {
        this.selectedValue2 = selectedValue2;
    }

    private String selectedValue1;
    private String selectedValue2;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private SelectedValue() {
    }
    public static SelectedValue getInstance() {
        return instance;
    }
}
