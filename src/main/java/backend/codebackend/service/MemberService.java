package backend.codebackend.service;


import backend.codebackend.domain.SignInRequest;
import backend.codebackend.dto.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Transactional //데이터를 저장하거나 변경할때 트랜잭션이 있어야 한다.
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Member signIn(SignInRequest signInRequest) {
        String Login = signInRequest.getLogin();
        String pw = signInRequest.getPw();

        Member member = getMemberByLoginAndPw(Login, pw);

        return member;
    }

    public Member signUp(MemberForm memberForm) {
        Member member = new Member.Builder(memberForm.getLoginId(), memberForm.getPw(), memberForm.getPwcheck())
                .nickname(memberForm.getNickname())
                .pnum(memberForm.getPnum()) //MemberFrom으로 넘어온 변수들 Builder 클래스의 변수에 다 저장
                .certified(memberForm.getCertified()) //MemberFrom으로 넘어온 변수들 Builder 클래스의 변수에 다 저장
                .build(); //new User(this)가 반환되므로 저장된 변수를 가지고 있는 Builder 클래스가 Member 생성자에 반환됨.

        validateDuplicateMember(member); //중복 회원 검증

        //MemberForm으로 저장된 값들을 Member 객체에 저장함.
        memberRepository.save(member);
        return member;
    }

    //아이디
    public Member getMemberByLoginAndPw(String Login, String pw) {
        //값이 없는 에러 발생 시
        try {
            memberRepository.findById(Login).get();
        } catch (NoSuchElementException e) {
            return null;
        }

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

    public Optional<Member> findOne(String memberId) {
        return memberRepository.findById(memberId);
    }
}
