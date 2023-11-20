package backend.codebackend.controller;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.Basket;
import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MozipService mozipService;
    private final MemberService memberService;
    private final ChatUserService chatUserService;
    private final AccountService accountService;
    private final BasketService basketService;

    @GetMapping("/main_page.html")
    public String list(Model model, HttpServletRequest request) {
        List<Mozip> mozipFormList = mozipService.getMozipList();
        model.addAttribute("postList", mozipFormList);


        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        //세션ID에 저장된 로그인 한 ID를 가져온 후 memberSerice에서 해당 로그인 ID에 해당하는 nickname 가져옴
        //create2(mozipForm);
        model.addAttribute("nickname",
                memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname());

        return "main_page"; //글 생성 시 다시 초기화면으로
    }

//    @GetMapping("/mozip/storeList")
//    @ResponseBody
//    public List<Restuarant> storeList(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        return restaurantService.RsData(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getAddress());
//    }


    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        return "세션 출력";
    }

    //채팅방 입장시 id에 해당하는 mozip 객체 넘겨줌
    @GetMapping("/mozip/chat/room")
    public String roomDetail(Model model, Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname();

        //정산 상태 확인
        if (mozipService.mozipStatus(id) && chatUserService.isDuplicateName(id, nickname)) {
            return "정산 시작된 상태입니다!";
        }

        System.out.println("입장 성공!!" + id);

        //채팅방 객체 넘겨줌
        model.addAttribute("room", mozipService.findRoomById(id).get());
        //닉네임 넘겨줌
        model.addAttribute("nickname", nickname);
        return "chat";
    }

    @GetMapping("/mozip/chat/myChatroom")
    @ResponseBody
    public Long myChatRoom(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname();

        //자기가 접속한 채팅방 아이디 찾기
        Long id = chatUserService.findMemberRoomId(nickname);

        return id;
    }


    //모집글 생성
    @PostMapping("/mozip")
    @ResponseBody
    public ResponseEntity<?> createMozip(String mozipTitle, int mozipRange, String mozipCategory, String mozipStore, int mozipPeople, HttpServletRequest request) { //세션에 저장된 id값의 닉네임을 가져오기 위해 request 선언
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "세션이 없습니다. 다시 로그인 해주세요."));
        }

        //세션을 통한 닉네임 조회
        Optional<Member> member = memberService.findLoginId(String.valueOf(session.getAttribute("memberId")));
        if (member.isEmpty())
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "사용자가 조회되지 않습니다."));

        String nickname = member.get().getNickname();
        //기존에 생성한 방이 있을 경우 생성 제한
        if (mozipService.findNickName(nickname).isPresent())
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "모집글은 한개만 생성 가능합니다!"));

        MozipForm mozipForm = MozipForm.builder()
                .title(mozipTitle)
                .distance_limit((long) mozipRange)
                .categories(mozipCategory)
                .store(mozipStore)
                .peoples(mozipPeople)
                .nickname(nickname)
                .build();

        //세션ID에 저장된 로그인 한 ID를 가져온 후 memberSerice에서 해당 로그인 ID에 해당하는 nickname 가져옴
        Mozip mozip = mozipService.savePost(mozipForm);
        //채팅방에 방 생성자 추가
        chatUserService.addUser(mozip.getId(), nickname);
        return ResponseEntity.ok("redirect:/main_page.html");
    }

    @GetMapping("/mozip/chat/chkRoomUserCnt")
    @ResponseBody
    public boolean chkRoomUserCnt(Long id, String nickname) {
        if (chatUserService.isDuplicateRoom(id, nickname)) { //기존에 입장 되어 있는 경우 들어가짐
            System.out.println("\n\n\n현재 인원 : " + mozipService.findRoomById(id).get().getUsercount());
            return true;
        }
        System.out.println("\n\n\n입장 실패 ㅜㅜ");
        return false;
    }

    //마이페이지로 이동
    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        Account account = accountService.findAccount(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getId());

        model.addAttribute("nickname",
                memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname());
        model.addAttribute("account", account);


        return "myPage";
    }

    //결제내역 페이지 이동 시
    @GetMapping("/Payment_details.html")
    public String payment_details(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        model.addAttribute("nickname",
                memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname());

        return "Payment_details"; //글 생성 시 다시 초기화면으로
    }

    @PostMapping("/mozip/deleteChatRoom")
    public String deleteChatRoom(Long id ,HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        // 세션ID에 저장된 로그인 한 ID를 가져온 후 memberSerice에서 해당 로그인 ID에 해당하는 nickname 가져옴
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId")))
                .get().getNickname();

        if (chatUserService.findByNickname(id, nickname)) {
            mozipService.deleteChatUsers(id);
            mozipService.deleteMozip(id);

            return "redirect:/main_page.html";
        } else {
            return "방장이 아닙니다";
        }
    }
}
