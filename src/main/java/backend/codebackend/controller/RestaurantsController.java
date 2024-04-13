package backend.codebackend.controller;

import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.Restuarant;
import backend.codebackend.repository.MozipRepository;
import backend.codebackend.service.BasketService;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.RestaurantService;
import com.google.api.Http;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantsController {
    private final MemberService memberService;
    private final RestaurantService restaurantService = RestaurantService.getInstance();
    private final MozipRepository mozipRepository;

    //사용자의 세션에 저장된 id를 통해 주소를 받아서 주소 출력
    @GetMapping("/mozip/storeList")
    @ResponseBody
    public List<Restuarant> storeList(HttpServletRequest request) throws InterruptedException {
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("memberId");

        WebDriver driver = restaurantService.driver(memberId);
        WebDriverWait wait = restaurantService.getWait(memberId);
        restaurantService.loadPage(driver);
        restaurantService.searchAddress(memberService.findLoginId(memberId).get().getAddress(), driver, wait);
        return restaurantService.RsData(wait);
    }

    @GetMapping("/mozip/categoryList")
    @ResponseBody
    public List<Restuarant> categoryList(HttpServletRequest request, String category) {
        HttpSession session = request.getSession(false);
        // 나중에 ResponseEntity로 리턴되게 변경
        WebDriverWait wait = restaurantService.getWait((String) session.getAttribute("memberId"));
        restaurantService.selectCategory(category, wait);
        return restaurantService.RsData(wait);
    }

    @PostMapping("/mozip/closeDriver")
    public void categoryList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        try {
            restaurantService.quitDriver((String) session.getAttribute("memberId"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/chat/menuList")
    @ResponseBody
    public Menu menuList(Long roomId, HttpServletRequest request) throws ExecutionException, InterruptedException {
        HttpSession session = request.getSession(false);
        Optional<Mozip> mozip = mozipRepository.findById(roomId);

        if(mozip.isPresent()) { //채팅방 id에 맞는 채팅방이 조회되었을 시
            if(mozip.get().getStore() == null) //선택한 가게가 없을 시
                log.info("가게를 지정하지 않았습니다.");
            String memberId = (String) session.getAttribute("memberId");
            WebDriver driver = restaurantService.driver(memberId);
            WebDriverWait wait = restaurantService.getWait(memberId);

            Future<Menu> m = restaurantService.menuList(mozip.get().getStore()
                        , memberService.findLoginId(memberId).get().getAddress(), driver, wait, (String) session.getAttribute("memberId"));
            Menu menu = m.get();
            return menu;
        }

        log.info("채팅방이 조회되지 않습니다.");
        return null;
    }

}
