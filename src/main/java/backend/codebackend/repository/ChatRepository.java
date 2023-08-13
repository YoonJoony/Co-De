package backend.codebackend.repository;

import backend.codebackend.dto.ChatRoomDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.util.UUID.randomUUID;

@Slf4j
public class ChatRepository {
    private Map<String, ChatRoomDto> chatRoomDtoMap;

    @PostConstruct //의존성 주입 후 초기화 작업이 필요한 메서드에 사용됨
    private void init() {
        chatRoomDtoMap = new LinkedHashMap<>(); //LinkedHashMap 은 리스트 생성 순서를 지켜서 생성됨.
    }

    //전체 채팅방 조회
    public List<ChatRoomDto> findAllRoom() {
        //채팅방 생성 순서를 최근순으로 반환
        List chatRooms = new ArrayList<>(chatRoomDtoMap.values());
        Collections.reverse(chatRooms); //파라미터로 받은 chatRooms를 거꾸로 뒤집음(최근순)

        return chatRooms;
    }

    //roomID 기준으로 채팅방 찾기
    public ChatRoomDto findRoomById(String roomId) {
        return chatRoomDtoMap.get(roomId);
    }

    //roomName로 채팅방 만들기
    public ChatRoomDto createChatRoom(String roomName, String roomPwd, boolean secretChk, int maxUserCnt) {
        ChatRoomDto chatRoomDTO = ChatRoomDto.builder() //채팅룸 이름으로 채팅방 생성
                        .roomId(UUID.randomUUID().toString())
                        .roomName(roomName)
                        .roomPwd(roomPwd)
                        .secretChk(secretChk)
                        .userlist(new HashMap<String, String>())
                        .userCount(0)
                        .maxUserCnt(maxUserCnt)
                        .build();

        //map에 채팅룸 아이디와 만들어진 채팅룸을 저장
        chatRoomDtoMap.put(chatRoomDTO.getRoomId(), chatRoomDTO);

        return chatRoomDTO;
    }

    //채팅방 인원 +1
    public void plusUserCnt(String roomId) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);
        room.setUserCount(room.getUserCount()+1);
    }

    //채팅방 인원-1
    public void minusUserCnt(String roomId) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);
        room.setUserCount(room.getUserCount()-1);
    }

    //채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String userName) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);
        String userUUID = randomUUID().toString();

        //아이디 중복 확인 후 userList에 추가
        room.getUserlist().put(userUUID, userName);

        return userUUID;
    }

    //채팅방 유저 이름 중복 확인
    public String isDuplicateName(String roomId, String username) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);
        String tmp = username;

        //만약 userName이 중복이라면 랜덤한 숫자를 붙임
        //이때 랜덤한 숫자를 붙였을 때 getUserlist안에 있는 닉네임이라면 다시 랜덤한 숫자를 부여
        //하지만 난 중복일 수가 없으니 걍 false로 해야됨
        if(room.getUserlist().containsValue(tmp)) {
            int ranNum = (int)(Math.random()*100)+1;

            tmp = username + ranNum;
        }

        return tmp;
    }

    //채팅방 유저 리스트 삭제
    public void delUser(String roomId, String userUUID) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);
        room.getUserlist().remove(userUUID);
    }

    //채팅방 userName 조회
    public String getUserName(String roomId, String userUUID) {
        ChatRoomDto roomDTO = chatRoomDtoMap.get(roomId);
        return roomDTO.getUserlist().get(userUUID);
    }

    //채팅방 전체 userList 조회
    public ArrayList<String> getUserList(String roomId) {
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDto room = chatRoomDtoMap.get(roomId);

        //hashmap을 for문을 돌린 후
        //value 값만 뽑아내서 list에 저장 후 return
        room.getUserlist().forEach((key, value) -> list.add(value));

        return list;
    }

    //maxUserCnt에 따른 채팅방 입장 여부
    public boolean chkRoomUserCnt(String roomId) {
        ChatRoomDto room = chatRoomDtoMap.get(roomId);

        log.info("참여인원 확인 [{}, {}]", room.getUserCount(), room.getMaxUserCnt());

        if(room.getUserCount()+1 > room.getMaxUserCnt()) {
            return false;
        }
        return true;
    }

    //채팅방 비밀번호 조회
    public boolean confirmPwd(String roomId, String roomPwd) {
        return roomPwd.equals(chatRoomDtoMap.get(roomId).getRoomPwd());
    }

    //채팅방 삭제
    public void delChatRoom(String roomId) {
        try {
            //채팅방 삭제
            chatRoomDtoMap.remove(roomId);

            log.info("삭제 완료 roonId : {}", roomId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
