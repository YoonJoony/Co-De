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


    @PostMapping(value = "/member/login")
    public String SignIn(SignInRequest signInRequest) {
        Member member = memberService.signIn(signInRequest);

        if(member == null) {
            return "errorpage";
        }

        return "Cotegory/cotegory.html";
    }

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
