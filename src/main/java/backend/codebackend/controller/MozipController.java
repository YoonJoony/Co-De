package backend.codebackend.controller;

import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.Restuarant;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.ChatUserService;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.MozipService;
import backend.codebackend.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MozipController {

    private final MozipService mozipService;
    private final MemberService memberService;
    private final ChatUserService chatUserService;

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
    public String roomDetail(Model model, Long id,  HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        System.out.println("입장 성공!!" + id);

        model.addAttribute("room", mozipService.findRoomById(id).get());
        model.addAttribute("nickname",
                memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname());
        return "chat";
    }

    //모집글 생성
    @PostMapping("/mozip")
    @ResponseBody
    public String createMozip(String mozipTitle,int mozipRange,String mozipCategory,String mozipStore,int mozipPeople, HttpServletRequest request) { //세션에 저장된 id값의 닉네임을 가져오기 위해 request 선언
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        MozipForm mozipForm = MozipForm.builder()
                .title(mozipTitle)
                .distance_limit((long) mozipRange)
                .categories(mozipCategory)
                .store(mozipStore)
                .peoples(mozipPeople)
                .nickname(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname())
                .build();

        //세션ID에 저장된 로그인 한 ID를 가져온 후 memberSerice에서 해당 로그인 ID에 해당하는 nickname 가져옴
        mozipService.savePost(mozipForm);
        return "redirect:/main_page.html"; //글 생성 시 다시 초기화면으로
    }

    @GetMapping("/mozip/chat/chkRoomUserCnt")
    @ResponseBody
    public boolean chkRoomUserCnt(Long id, String nickname) {
        if(!chatUserService.isDuplicateName(id, nickname)){ //기존에 입장 되어 있는 경우 들어가짐
            System.out.println("\n\n\n현재 인원 : " + mozipService.findRoomById(id).get().getUsercount());
            log.info("이미 입장해 계십니다.");
            return true;
        }
        else if(mozipService.chkRoomUserCnt(id)) {
            mozipService.plusUserCnt(id);
            System.out.println("\n\n\n현재 인원 : " + mozipService.findRoomById(id).get().getUsercount());
            log.info("신입이군요");
            return true;
        }
        System.out.println("\n\n\n입장 실패 ㅜㅜ");
        return false;
    }
}
