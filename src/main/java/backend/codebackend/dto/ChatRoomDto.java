package backend.codebackend.dto;


import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

//Stomp를 통해 pub/sub를 사용하면 구독자 관리가 알아서 된다.
//따라서 따로 세션 관리를 하는 코드를 작성할 필요 없고,
//메시지 다른 세션의 클라이언트에게 발송하는 것도 구현 필요가 없다.
@Data
@Builder
public class ChatRoomDto {
    private String roomId; //채팅방 아이디
    private String roomName; //채팅방 이름
    private long userCount; //채팅방 인원 수
    private int maxUserCnt; //채티앙최대 인원 제한

    private String roomPwd; //채팅방 삭제시 필요한 pwd
    private boolean secretChk; //채팅방 잠금 여부

    private HashMap<String, String> userlist = new HashMap<String, String>();
}
