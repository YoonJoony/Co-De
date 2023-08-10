package backend.codebackend.controller;



//채팅을 수신(Sub)하고, 송신(pub) 하기 위한 Controller
//@MessageMapping : Stomp에서 들어오는 message를 서버에서 발송(pub)한 메시지가 도착하는 엔드포인트다.
//여기서 "/chat/enterUser" 로 되어있지만 실제로는 앞에 "/pub"가 생략되어 있다.
//이 주소에 메시지를 발송하면 @MessageMapping에 의해서 아래의 해당 어노테이션이 달린 메서드가 실행된다.

//@convertAndSend() : 이 메서드는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
//이를 통해서 도착 지점 즉 sub가 되는 지점으로 인자로 들어온 객체를 Message객체로 변환해서
//해당 도착지점을 sub하고 있는 모든 사용자에게 메시지를 보내주게 된다.

import backend.codebackend.dto.ChatDTO;
import backend.codebackend.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatRepository chatRepository;

    //MessageMapping을 통해 webSocket로 들어오는 메시지를 발신 처리한다.
    //이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아 처리한다.
    //처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {//Payload : Message전송할 데이터가 담긴 Wrapper Class로 메시지 요청 시 instance화 된 Message를 생성한다. 그것을 ChatDTO 와 매핑시켜줌
        //SimpMessageHeaderAccessor : 메시지의 헤더를 읽고 쓰기위한 클래스
        //채팅방 유저 +1
        chatRepository.plusUserCnt(chat.getRoomId());

        //채티방에 유저 추가 및 UserUUID 반환
        String userUUID = chatRepository.addUser(chat.getRoomId(), chat.getSender());

        //반환 결과를 socket session에 userUUID로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
    }
}
