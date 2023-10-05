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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Future;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantsController {
    private final MemberService memberService;
    private RestaurantService restaurantService;

    //사용자의 세션에 저장된 id를 통해 주소를 받아서 주소 출력
    @GetMapping("/mozip/storeList")
    @ResponseBody
    public List<Restuarant> storeList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        restaurantService = new RestaurantService();
        restaurantService.driver();
        restaurantService.loadPage();
        restaurantService.searchAddress(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getAddress());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return restaurantService.RsData();
    }

    @GetMapping("/mozip/categoryList")
    @ResponseBody
    public List<Restuarant> categoryList(String category) {
        restaurantService.selectCategory(category);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return restaurantService.RsData();
    }

    @PostMapping("/mozip/closeDriver")
    public void categoryList() {
        try {
            restaurantService.quitDriver();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/mozip/menuList")
    @ResponseBody
    public Future<Menu> menuList(String restaurantTitle, String address) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return restaurantService.menuList(restaurantTitle, address);
    }


}
