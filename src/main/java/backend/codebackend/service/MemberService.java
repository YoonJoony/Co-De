package backend.codebackend.service;


import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getNickname())
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
