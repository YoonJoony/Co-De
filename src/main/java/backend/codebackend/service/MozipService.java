package backend.codebackend.service;

import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.repository.DeliveryInfoRepository;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.MozipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class MozipService {
    private final MozipRepository mozipRepository; //final을 해줘야 config에서 생성자 주입이 됨.

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
    public Optional<Mozip> findNickName(String nickname) {
        return mozipRepository.findByName(nickname);
    }

    //이미 방에 입장 해 있는 경우 다시 접속 시
    public boolean isDuplicateName(Long id, String ninkname) {
        return false;
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
        if(mozip.getUsercount()+1 > mozip.getPeoples()) {
            System.out.println("입장 인원 초과");
            return false;
        }
        System.out.println("입장 가능.\n인원 : " + mozip.getUsercount());
        return true;
    }

    //채팅방 인원 -1
    public void minusUserCnt(Long id) {
        mozipRepository.minusUserCnt(id);
    }

    //모집글 정산 상태 확인
    public boolean mozipStatus(Long id) {
        return mozipRepository.mozipStatus(id);
    }

    //정산 상태 변경 (정산시작)
    public void calculateStartStatus(Long id) {
        mozipRepository.calculateStartStatus(id);
    }

    //정산 상태 변경 (정산 전)
    public void preCalculateStartStatus(Long id) {
        mozipRepository.preCalculateStartStatus(id);
    }

    // 모집글 삭제
    public void deleteMozip(Long id) {
        mozipRepository.deleteMozip(id);
    }

    // 채팅방 유저 삭제
    public void deleteChatUsers(Long id) {
        mozipRepository.deleteChatUsers(id);
    }
}
