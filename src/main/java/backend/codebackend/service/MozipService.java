package backend.codebackend.service;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.MozipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class MozipService {
    private final MozipRepository mozipRepository; //final을 해줘야 config에서 생성자 주입이 됨.
    private final MemberRepository memberRepository;

    public Mozip savePost(MozipForm mozipForm) {
        Mozip mozip = mozipForm.toEntity();
        mozipRepository.save(mozip);
        mozipForm.setUsercount(1);
        return mozip;
    }

    //모집글 id 조회
    public Optional<Mozip> findRoomById(Long id) {
        return mozipRepository.findById(id);
    }
    
    //닉네임 조회
    public String findNickName(String nickname) {
        Member member = memberRepository.findByName(nickname).get();
        System.out.println("이름 : "+ member.getNickname());
        return member.getNickname();
    }

    //게시글 가져오기
    public List<Mozip> getMozipList() {
        List<Mozip> mozipList = mozipRepository.findAll();

        return mozipList;
    }

    //채팅방 인원 +1
    public void plusUserCnt(Long id) {
        mozipRepository.plusUserCnt(id);
    }

    public boolean chkRoomUserCnt(Long id) {
        Mozip mozip = mozipRepository.findById(id).get();
        if(mozip.getUsercount()+1 > mozip.getPeoples())
            return false;
        return true;
    }

    //채팅방 인원 -1
    public void minusUserCnt(Long id) {
        mozipRepository.minusUserCnt(id);
    }

}
