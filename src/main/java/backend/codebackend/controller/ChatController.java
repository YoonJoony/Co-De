package backend.codebackend.controller;



//채팅을 수신(Sub)하고, 송신(pub) 하기 위한 Controller
//@MessageMapping : Stomp에서 들어오는 message를 서버에서 발송(pub)한 메시지가 도착하는 엔드포인트다.
//여기서 "/chat/enterUser" 로 되어있지만 실제로는 앞에 "/pub"가 생략되어 있다.
//이 주소에 메시지를 발송하면 @MessageMapping에 의해서 아래의 해당 어노테이션이 달린 메서드가 실행된다.

//@convertAndSend() : 이 메서드는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
//이를 통해서 도착 지점 즉 sub가 되는 지점으로 인자로 들어온 객체를 Message객체로 변환해서
//해당 도착지점을 sub하고 있는 모든 사용자에게 메시지를 보내주게 된다.

import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.ChatDTO;
import backend.codebackend.repository.MozipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final MozipRepository mozipRepository;

//    @GetMapping("/mozip/chat/enterUser")
//    public void enterUser() {
//        mozipRepository.plusUserCnt();
//    }
}
