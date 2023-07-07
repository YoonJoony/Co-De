package backend.codebackend.service;


import backend.codebackend.controller.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public String join(MemberForm memberForm) {

        Member member = new Member.Builder(memberForm.getId(), memberForm.getPw(), memberForm.getPwcheck())
                .nickname(memberForm.getNickname())
                .pnum(memberForm.getPnum()) //MemberFrom으로 넘어온 변수들 Builder 클래스의 변수에 다 저장
                .certified(memberForm.getCertified()) //MemberFrom으로 넘어온 변수들 Builder 클래스의 변수에 다 저장
                .build(); //new User(this)가 반환되므로 저장된 변수를 가지고 있는 Builder 클래스가 Member 생성자에 반환됨.

        validateDuplicateMember(member); //중복 회원 검증

        //MemberForm으로 저장된 값들을 Member 객체에 저장함.
        memberRepository.save(member);
        return member.getId();
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
