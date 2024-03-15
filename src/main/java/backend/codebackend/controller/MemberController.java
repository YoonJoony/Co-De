package backend.codebackend.controller;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.SignInRequest;
import backend.codebackend.dto.MemberForm;
import backend.codebackend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    public static Hashtable sessionList = new Hashtable();

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //Session이 없으면 null return

        //세션이 있으면 기존 섹션 삭제
        //SessionId는 쿠키값이다.
        if(session != null) {
            session.invalidate();
        }

        //세션리스트 파기
        sessionList.remove(session.getId());

        return "redirect:/";
    }


    //로그인
    @PostMapping(value = "/login")
    public String SignIn(@ModelAttribute SignInRequest signInRequest, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        //로그인 후 아이디가 데이터베이스에 존재하는지 검사
        Member member = memberService.signIn(signInRequest);

        if(member == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 정보를 다시 입력해 주십시오");
            return "redirect:/login.html";
        }

        //세션 생성하기 전 기존 세션 파기
        //httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true); //세션 없으면 생성.

        //세션에 아이디 넣어줌
        session.setAttribute("memberId", member.getLogin());
        session.setMaxInactiveInterval(1800); //session이 30분동안 유지

        //세션리스트에 저장
        sessionList.put(session.getId(), session);

        //성공 시 메인 페이지 이동
        return "redirect:/main_page.html";
    }

    // 회원 탈퇴
    @GetMapping("/withdraw")
    public String withdrawMember(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false); // 현재 세션 가져오기

        if (session != null) {
            String memberId = (String) session.getAttribute("memberId");
            if (memberId != null) {
                memberService.withdrawMember(memberId); // 회원 탈퇴 메서드 호출
                session.invalidate(); // 세션 무효화
                redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 완료되었습니다.");
                return "redirect:/logout"; // 로그아웃 페이지나 다른 페이지로 리다이렉트
            }
        }

        redirectAttributes.addFlashAttribute("errorMessage", "세션에 로그인 정보가 없습니다.");
        return "redirect:/login.html"; // 세션에 로그인 정보가 없을 경우 다시 로그인 페이지로 리다이렉트
    }

    //회원가입 성공 시 처음 화면으로 이동
    @PostMapping(value = "/members/new")
    public String create(@ModelAttribute MemberForm form) {
        memberService.signUp(form);

        //세션 생성 전 기존의 세션 파기
        return "redirect:/";
    }


//    @GetMapping("/members")
//    public String list(Model model) {
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members", members);
//        return "members/memberList";
//    }

    //!!!! 다른 브라우저를 써야 세션 리스트가 나옴.
    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();
        Map<String, String> lists = new HashMap<>();
        while(elements.hasMoreElements()) {
            HttpSession session = (HttpSession) elements.nextElement();
            lists.put(session.getId(), String.valueOf(session.getAttribute("memberId")));
        }
        return lists;
    }

}
