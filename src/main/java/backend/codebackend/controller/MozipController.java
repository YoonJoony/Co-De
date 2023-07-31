package backend.codebackend.controller;

import backend.codebackend.domain.SelectedValue;
import backend.codebackend.dto.MozipForm;
import backend.codebackend.service.MozipService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MozipController {

    private final MozipService mozipService;
    private SelectedValue sv = SelectedValue.getInstance(); //싱글톤으로 객체 인스턴스 1개만 생성되도록 함
//    private String selectedValue1;
//    private String selectedValue2;

    @GetMapping("/")
    public String test1() {
        return "main_page";
    }

    //모집글 생성(title, distance_limit) 변수 저장
    @PostMapping("/mozip")
    public String createMozip1(@ModelAttribute MozipForm mozipForm) {
        create(mozipForm);

        return "main_page";
    }

    //모집글 생성(caterory, people) 변수 저장
    @PostMapping("/mozipCreate")
    public String createMozip(@RequestParam(value = "selectedValue1", required = false) String selectedValue1,
                              @RequestParam(value = "selectedValue2", required = false) String selectedValue2) {
        System.out.println(selectedValue1);
        System.out.println(selectedValue2);

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
        mozipForm.setPeoples(sv.getSelectedValue2());


        mozipService.savePost(mozipForm); //모집글 저장.

    }


//    모집 생성창 카테고리 선택, 인원 선택 저장
//    @PostMapping("/mozip")
//    public String yourControllerMethod(@RequestParam(value = "selectedValue1", required = false) String selectedValue1,
//                                       @RequestParam(value = "selectedValue2", required = false) String selectedValue2) {
//        System.out.println(selectedValue1);
//        System.out.println(selectedValue2);
//        // 전달된 값 처리 코드
//        return "main_page";
//    }




//    @GetMapping("/list")
//    public String list(Model model) {
//        List<MozipForm> MozipdtoList = mozipService.getMozipList();
//        model.addAttribute("postList", MozipdtoList);
////        return "board/list.html";
////    }


}
