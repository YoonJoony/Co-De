package backend.codebackend.service;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.ChatRoomDto;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.dto.MozipMap;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.MozipRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class MozipService {
    private final MozipRepository mozipRepository; //final을 해줘야 config에서 생성자 주입이 됨.
    private final MemberRepository memberRepository;

    public Mozip savePost(MozipForm mozipForm) {
        Mozip mozip = mozipForm.toEntity();
        mozipRepository.save(mozip);
        mozipForm.setUsercount(1);
        MozipMap.getInstance().getMozips().put(mozip.getId(), mozipForm); //모집글id에 해당하는 모집글 담음
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

    //채팅방 리스트에 유저 추가
    public String addUser(Long id, String nickname) {
        Mozip mozip = mozipRepository.findById(id).get();
        MozipForm mozipForm = MozipMap.getInstance().getMozips().get(mozip.getId());
        String userUUID = UUID.randomUUID().toString();

        ConcurrentMap<String, String> userList = (ConcurrentMap<String, String>)mozipForm.getUserList();
        userList.put(userUUID, nickname);

        return userUUID;
    }

    //채팅방 전체 userlist 조회
    public ArrayList<String> getUserList(Long id) {
        ArrayList<String> list = new ArrayList<>();

        MozipForm mozipForm = MozipMap.getInstance().getMozips().get(id);

        // hashmap 을 for 문을 돌린 후
        // value 값만 뽑아내서 list 에 저장 후 reutrn
        mozipForm.getUserList().forEach((key, value) -> list.add((String) value));
        return list;
    }

    //특정 유저 삭제
    public void delUser(Long id, String nickname, String userUUID) {
        //유저 퇴장 시 세션에 현재 접속해 있던 해당 유저의 UUID의 값을 가져와서 리스트에서 삭제시킴.
        MozipMap.getInstance().getMozips().get(id).getUserList().remove(userUUID);
    }
}
