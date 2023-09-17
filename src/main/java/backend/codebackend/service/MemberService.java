package backend.codebackend.service;


import backend.codebackend.domain.SignInRequest;
import backend.codebackend.dto.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Transactional //데이터를 저장하거나 변경할때 트랜잭션이 있어야 한다.
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class MemberService {
    private final MemberRepository memberRepository;

    //로그인
    public Member signIn(SignInRequest signInRequest) {
        //로그인 한 아이디와 비밀번호를 받음
        String Login = signInRequest.getLogin();
        String pw = signInRequest.getPw();

        //데이터베이스에 로그인 한 아이디가 존재하는지 확인 없으면 null
        Member member = getMemberByLoginAndPw(Login, pw);

        return member;
    }
    
    //회원가입
    public Member signUp(MemberForm memberForm) {
        Member member = memberForm.toEntity(); //MemberForm toEntitiy() 메소드로 회원가입에서 받아온 객체 바로 member 변수로 객체화 하여 member타입으로 리턴...

        validateDuplicateMember(member); //중복 회원 검증

        //MemberForm으로 저장된 값들을 Member 객체에 저장함.
        memberRepository.save(member);
        return member;
    }

    //아이디
    public Member getMemberByLoginAndPw(String Login, String pw) {
        //값이 없는 에러 발생 시 (db에 로그인 한 아이디가 없을 시) 에러 캐치해서 null 리턴
        try {
            memberRepository.findById(Login).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        
        //에러 발생 안하면 로그인에 해당하는 아이디에 객체 member 리턴받음
        Member member = memberRepository.findById(Login).get();

        //비밀번호가 다르면 null 출력
        if(!pw.equals(member.getPw())) {
            return null;
        }
        
        return member;
    }

    private void validateDuplicateMember(Member Member) {
        memberRepository.findByName(Member.getNickname())
                .ifPresent(m -> { //값이 있으면 특정 로직이 동작한다. optional이기 때문에 optional 메소드 ifPresent() 사용가능.
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findLoginId(String login) {
        return memberRepository.findById(login);
    }

}
