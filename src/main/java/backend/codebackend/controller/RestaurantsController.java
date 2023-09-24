package backend.codebackend.controller;

import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Restuarant;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Future;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantsController {

    private final RestaurantService restaurantService;
    private final MemberService memberService;
    
    
    //사용자의 세션에 저장된 id를 통해 주소를 받아서 주소 출력
    @GetMapping("/mozip/storeList")
    @ResponseBody
    public List<Restuarant> storeList(String category, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return restaurantService.RsData(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getAddress(), category);
    }

//    @GetMapping("/mozip/menuList")
//    @ResponseBody
//    public Menu menuList(String restaurantTitle, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        Future<Menu> m = restaurantService.menuList(restaurantTitle, memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getAddress());
//        return m.get();
//    }
}
