package backend.codebackend.controller;

import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.MozipService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MozipController {

    private MozipService mozipService;

    public MozipController(MozipService mozipService) {
        this.mozipService = mozipService;
    }

    @GetMapping("/")    //모집 글 생성 후 리스트들을 보여주는 화면
    public String list() {
        return "mozip/MozipList.html";
    }

    @GetMapping("/post")    //모집 글 생성하는 화면
    public String post() {
        return "mozip/create_text.html";
    }

    @PostMapping("/post")
    public String createMozip(MozipForm mozipForm) {
        MozipService.savePost(mozipForm);
        return "redirect:/";
    }
}
