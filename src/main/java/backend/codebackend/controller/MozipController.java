package backend.codebackend.controller;

import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.MozipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MozipController {

    private MozipService mozipService;


    @GetMapping("/post")    //모집 글 생성하는 화면
    public String post() {
        return "main_page";
    }

    @PostMapping("/post")
    public String createMozip(MozipForm mozipForm) {
        mozipService.savePost(mozipForm);
        return "redirect:/";
    }
}
