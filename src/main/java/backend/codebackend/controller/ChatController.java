package backend.codebackend.controller;



//채팅을 수신(Sub)하고, 송신(pub) 하기 위한 Controller
//@MessageMapping : Stomp에서 들어오는 message를 서버에서 발송(pub)한 메시지가 도착하는 엔드포인트다.
//여기서 "/chat/enterUser" 로 되어있지만 실제로는 앞에 "/pub"가 생략되어 있다.
//이 주소에 메시지를 발송하면 @MessageMapping에 의해서 아래의 해당 어노테이션이 달린 메서드가 실행된다.

//@convertAndSend() : 이 메서드는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
//이를 통해서 도착 지점 즉 sub가 되는 지점으로 인자로 들어온 객체를 Message객체로 변환해서
//해당 도착지점을 sub하고 있는 모든 사용자에게 메시지를 보내주게 된다.

import backend.codebackend.domain.ChatUser;
import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.ChatDTO;
import backend.codebackend.repository.MozipRepository;
import backend.codebackend.service.ChatUserService;
import backend.codebackend.service.MozipService;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final MozipRepository mozipRepository;
    private final MozipService mozipService;
    private final ChatUserService chatUserService;
    @MessageMapping("/mozip/chat/enterUser") //해당 주소로 메시지가 도착시 메소드 실행
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {//Payload : Message전송할 데이터가 담긴 Wrapper Class로 메시지 요청 시 instance화 된 Message를 생성한다. 그것을 ChatDTO 와 매핑시켜줌
    /*  @Payload : 메시지 핸들러에서 메시지를 처리할 때, 메시지의 payload를 추출하여 메서드 인자로 전달하는 기능
        - payload(데이터 전송 시 실제로 전송되는 데이터)를 명시적으로 지정하기 위해 사용됨
        여기선 socket.js에서 stompClisent.sender() 에서 이 주소로 JSON 형태의 chatDTO 객체에 들어갈 데이터가 전송되었다.
        따라서 그 데이터를 @Payload가 추출해서 ChatDTO 타입으로 추출하여 chat에 저장했다.

        SimpMessageHeaderAccessor : WebSocket 메시지의 헤더를 읽고 쓰기위한 클래스 WebSocket 메시지의 헤더를 읽고 쓸 수가 있음
     */

        //해당 닉네임이 이미 방에 없는 경우 아래 실행
        if(chatUserService.addUser(chat.getId(), chat.getSender())) {

            log.info("방 번호 : " + chat.getId() + " 접속한 사람 : " + chat.getSender());

            headerAccessor.getSessionAttributes().put("nickname", chat.getSender());
            headerAccessor.getSessionAttributes().put("roomId", chat.getId());
        /*  WebSocket은 HTTP 프로토콜 위에 동작해서 HTTP와 유사한 세션 관리 기능을 제공한다.
            따라서 클라이언트와 서버 간의 연결이 지속되기 때문에 이전에 수행한 요청과 응답 정보를 계속 유지할 필요가 있음.

            headerAccessor.getSessionAttributes() 메서드는 WebSocket 세션에서 속성 값을 가져오거나 설정하는데 사용된다.
            위에서는 userUUID와 roomId를 저장하는데 사용되었다.
            따라서 이후 클라이언트에 전송되는 모든 메시지에서 이 값을 참조할 수 있다.
         */
            chat.setMessage(chat.getSender() + "님 입장!!");
            //convertAndSend : 지정된 대상 주소로 메시지를 전송.
            //"/sub/chat/room/" + chat.getRoomId() => 메시지를 보낼 대상 주소를 의미. chat => 전송할 메시지 내용을 담은 객체
        }
        else {
            headerAccessor.getSessionAttributes().put("nickname", chat.getSender());
            headerAccessor.getSessionAttributes().put("roomId", chat.getId());
            chat.setMessage(chat.getSender() + "님이 다시 입장하셨습니다.");
            log.info(chat.getSender() + "님은 이미 방에 들어와 있습니다");
        }

        template.convertAndSend("/sub/mozip/chat/room/" + chat.getId(), chat);
    }

    @MessageMapping("/mozip/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/mozip/chat/room/"+chat.getId(),chat);
    }

    @GetMapping("/mozip/chat/userList")
    @ResponseBody
    public ArrayList<String> getUserList(Long id) {
        return chatUserService.getUserList(id);
    }

    @GetMapping("/mozip/chat/inDuplicateName")
    @ResponseBody
    public String inDuplicateName(String nickname) {

        return nickname;
    }
}
