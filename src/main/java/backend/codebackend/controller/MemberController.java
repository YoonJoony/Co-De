package backend.codebackend.controller;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.SignInRequest;
import backend.codebackend.dto.MemberForm;
import backend.codebackend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    //로그인
    @PostMapping(value = "/member/login")
    public String SignIn(SignInRequest signInRequest) {
        //로그인 후 아이디가 데이터베이스에 존재하는지 검사
        Member member = memberService.signIn(signInRequest);

        //없으면 에러 페이지를 보여줌
        if(member == null) {
            return "errorpage";
        }

        //성공 시 메인 페이지 이동
        return "Cotegory/cotegory.html";
    }

    //회원가입 성공 시 처음 화면으로 이동
    @PostMapping(value = "/members/new")
    public String create(MemberForm form) {
        memberService.signUp(form);

       return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
