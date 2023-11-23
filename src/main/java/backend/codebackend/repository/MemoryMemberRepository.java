package backend.codebackend.repository;

import backend.codebackend.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository{

    private static Map<String, Member> store = new HashMap<>();
    private static long sequence = 0L;


   @Override
   public Member save(Member member) {
       member.setId(++sequence); //member id값 셋팅
       store.put(member.getLogin(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id)); //키 값에 해당하는 값(value) 꺼냄
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getNickname().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void withdrawMember(String login) {
        Optional<Member> member = findByName(login);
        member.ifPresent(existingMember -> store.remove(existingMember.getLogin()));
    }
}
