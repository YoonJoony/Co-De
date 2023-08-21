package backend.codebackend.controller;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.SelectedValue;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.dto.MozipMap;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.MozipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MozipController {

    private final MozipService mozipService;
    private final MemberService memberService;
    private SelectedValue sv = SelectedValue.getInstance(); //싱글톤으로 객체 인스턴스 1개만 생성되도록 함
//    private String selectedValue1;
//    private String selectedValue2;

//    @GetMapping("/")
//    public String test1(Model model) {
//        return "main_page";
//    }

    @GetMapping("/main_page.html")
    public String list(Model model, HttpServletRequest request) { //리스트들을 한 줄이 아니라 벗츠처럼? 네모 칸 예쁘게 수정?
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
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        return "세션 출력";
    }
    
    //채팅방 입장시 id에 해당하는 mozip 객체 넘겨줌
    @GetMapping("/mozip/chat/room")
    public String roomDetail(Model model, Long id) {
        System.out.println("입장 성공!!");
        return "redirect:/main_page.html";
    }

    //모집글 생성(title, distance_limit) 변수 저장
    @PostMapping("/mozip")
    public String createMozip1(@ModelAttribute MozipForm mozipForm, HttpServletRequest request) { //세션에 저장된 id값의 닉네임을 가져오기 위해 request 선언
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        //세션ID에 저장된 로그인 한 ID를 가져온 후 memberSerice에서 해당 로그인 ID에 해당하는 nickname 가져옴
        sv.setNickname(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname());
        create(mozipForm);

        return "redirect:/main_page.html"; //글 생성 시 다시 초기화면으로
    }


    //모집글 생성(caterory, people) 변수 저장
    @PostMapping("/mozipCreate")
    public String createMozip(@RequestParam(value = "selectedValue1", required = false) String selectedValue1,
                              @RequestParam(value = "selectedValue2", required = false) String selectedValue2) {
        //카테고리, 인원 수가 저장되는 싱글톤 객체 안에 데이터로 넘어온 변수 저장해두기
        sv.setSelectedValue1(selectedValue1); //카테고리 저장
        sv.setSelectedValue2(selectedValue2); //인원 수 저장

        return "main_page";
    }
    //2번 post로 저장하는 이유 : 버튼 클릭시 html에서 넘어오는 form 태그에서 한번, ajax에서 넘어오는 데이터에서 또 한번
    //                        총 두번 데이터 전송이 실행되니때문에 저장하는 메소드를 나눠준 후 create에서 save

    //모집글 테이블에 저장
    public void create(MozipForm mozipForm) {
        mozipForm.setCategories(sv.getSelectedValue1()); //카테고리, 인원 수가 저장된 싱글톤 객체에서 값을 꺼내오기
        mozipForm.setPeoples(Integer.parseInt(sv.getSelectedValue2()));
        mozipForm.setNickname(sv.getNickname());
        mozipService.savePost(mozipForm); //모집글 저장.
    }

    @GetMapping("/mozip/chat/chkRoomUserCnt/{id}")
    @ResponseBody
    public boolean chkRoomUserCnt(@PathVariable Long id) {
        if(mozipService.chkRoomUserCnt(id)) {
            mozipService.plusUserCnt(id);
            System.out.println("\n\n\n현재 인원 : " + mozipService.findRoomById(id).get().getUsercount());
            return true;
        }
        System.out.println("\n\n\n입장 실패 ㅜㅜ");
        return false;
    }
}
