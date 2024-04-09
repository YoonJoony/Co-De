package backend.codebackend.dto;

import backend.codebackend.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public class MemberForm {

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String login;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "(?=.*[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣])(?=.*[0-9])(?=.*[^\\w\\s]).{4,20}",
            message = "길이가 8~20의 알파벳, 숫자, 특수문자가 각 1개이상 포함된 어야 합니다.")
    private String pw;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String pwcheck;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private  String username;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "길이가 3~20의 알파벳, 숫자, 한글만 허용 됩니다.")
    private String nickname;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String address;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$",
            message = "01로 시작하는 10-11자리 숫자여야 합니다.")
    private String pnum;

    @NotEmpty(message = "공백을 포함해선 안됩니다.")
    private String certified;

    public Member toEntity(){
        Member member = Member.builder()
                .login(login)
                .pw(pw)
                .pwcheck(pwcheck)
                .username(username)
                .nickname(nickname)
                .address(address)
                .pnum(pnum)
                .certified(certified)
                .build();
        return member;
    }
}
