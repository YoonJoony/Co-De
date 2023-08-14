package backend.codebackend.controller;

import backend.codebackend.dto.ChatRoomDto;
import backend.codebackend.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRepository chatRepository;

    //채팅 리스트 화면
    // /로 요청이 들어오면 전체 채티룸 리스트를 담아서 return
    @GetMapping("/")
    public String goChatRoom(Model model) {
        model.addAttribute("list", chatRepository.findAllRoom());
        //model.addAttribute("user", "hey");
        log.info("SHOW ALL ChatList {}", chatRepository.findAllRoom());
        return "/ChatRoom/roomlist";
    }

    //채팅방 생성
    //채팅방 생성 후 다시 / 로 return
    @PostMapping("/chat/createroom")
    public String createRoom(@RequestParam("roomName") String roomName, @RequestParam("roomPwd")String roomPwd, @RequestParam("secretChk")String secretChk,
                             @RequestParam(value = "maxUserCnt", defaultValue = "100")String maxUserCnt,  RedirectAttributes rttr) {
        ChatRoomDto room = chatRepository.createChatRoom(roomName, roomPwd, Boolean.parseBoolean(secretChk), Integer.parseInt(maxUserCnt));
        log.info("CREATE Chat Room {}", room);
        rttr.addFlashAttribute("roomName", room); //addFlashAttribute는 값을 전달한 후 데이터가 소멸된다.
        return "redirect:/";
    }

    //채팅방 입장 화면
    //파라미터로 넘어오는 roomId를 확인 후 해당 roomId를 기준으로
    //채팅방을 찾아서 클라이언트를 chatroom으로 보낸다.
    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId) {
        log.info("roomId {}", roomId);
        model.addAttribute("room", chatRepository.findRoomById(roomId));
        return "/ChatRoom/chatroom";
    }

    //채팅방 비밀번호 확인
    @PostMapping("/chat/confirmPwd/{roomId}")
    @ResponseBody
    public boolean confirmPwd(@PathVariable String roomId, @RequestParam String roomPwd) {
        //@PathVariable : url의 변수값 ex) {roomId}를 추출하여 변수에 저장한다.
        //넘어온 roomPwd를 이용해서 비밀번호 찾기
        //찾아서 입력받은 roomPwd와 진짜 방 비번이랑 비교해서 맞으면 T, or F
        return chatRepository.confirmPwd(roomId, roomPwd);
    }

    //채팅방 삭제
    @GetMapping("/chat/delRoom/{roomId}")
    public String delChatRoom(@PathVariable String roomId) {
        //roomId 기준으로 chatRoomMap에서 삭제
        chatRepository.delChatRoom(roomId);

        return "redirect:/";
    }

    // 유저 카운트
    @GetMapping("/chat/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chUserCnt(@PathVariable String roomId){

        return chatRepository.chkRoomUserCnt(roomId);
    }
}

