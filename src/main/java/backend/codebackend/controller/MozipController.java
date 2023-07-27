package backend.codebackend.controller;

import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.MozipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MozipController {

    private MozipService mozipService;

    @GetMapping("/")    //모집 글 생성 후 리스트들을 보여주는 화면
    public String list(Model model) {
        List<MozipForm> mozipFormList = mozipService.getMozipList();
        model.addAttribute("postList", mozipFormList);
        return "mozip/MozipList.html";
    }

    @GetMapping("/post")    //모집 글 생성하는 화면
    public String post() {
        return "mozip/create_text.html";
    }

    @PostMapping("/post")
    public String createMozip(MozipForm mozipForm) {
        mozipService.savePost(mozipForm);
        return "redirect:/";
    }
}
