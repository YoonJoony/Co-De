package backend.codebackend.service;

import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(String Id, String pw){
        return memberRepository.findById(Id)
                .filter(member -> member.getPw().equals(pw))
                .orElse(null);
    }
}
