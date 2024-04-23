package backend.codebackend.controller;

import backend.codebackend.domain.Member;
import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.Restuarant;
import backend.codebackend.repository.MozipRepository;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.MenuService;
import backend.codebackend.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantsController {
    private final MemberService memberService;
    private final RestaurantService restaurantService = RestaurantService.getInstance();
    private final MozipRepository mozipRepository;
    private final MenuService menuService;

    @GetMapping("/mozip/storeList")
    public ResponseEntity<?> storeList(HttpServletRequest request) throws InterruptedException {
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("memberId");
        Optional<Member> member = memberService.findLoginId(memberId);
        if(member.isEmpty())
            return ResponseEntity.badRequest().body("사용자가 조회되지 않습니다.");

        WebDriver driver = restaurantService.driver(memberId);
        WebDriverWait wait = restaurantService.getWait(memberId);
        restaurantService.loadPage(driver);
        restaurantService.searchAddress(member.get().getAddress(), driver, wait);
        List<Restuarant> restaurants = restaurantService.RsData(wait);
        if(restaurants.isEmpty())
            return ResponseEntity.badRequest().body("주변 가게가 존재하지 않거나 크롤링 오류");
        else
            return ResponseEntity.ok(restaurants);
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

    @PostMapping("/mozip/menuList")
    public ResponseEntity<?> menuList(Long mozipId, HttpServletRequest request) throws ExecutionException, InterruptedException {
        HttpSession session = request.getSession(false);
        Optional<Mozip> mozip = mozipRepository.findById(mozipId);

        if(mozip.isPresent()) {
            String memberId = (String) session.getAttribute("memberId");
            WebDriver driver = restaurantService.driver(memberId);
            WebDriverWait wait = restaurantService.getWait(memberId);

            CompletableFuture<List<Menu>> future = restaurantService.menuList(mozip.get(), driver, wait, memberId);
            List<Menu> menus = future.get();

            menuService.menuSave(menus);

            if(menus.isEmpty())
                return ResponseEntity.badRequest().body("메뉴가 제대로 되지 않았습니다.");
            else
                return ResponseEntity.ok("메뉴가 성공적으로 저장 되었습니다.");
        }

        return ResponseEntity.badRequest().body("채팅방이 조회되지 않았습니다.");
    }

    @GetMapping("/chat/mozip/menuListSelect")
    public ResponseEntity<?> menuListSelect(Long roomId) {
        Optional<Mozip> mozip = mozipRepository.findById(roomId);

        if(mozip.isPresent()) {
            List<List<Menu>> menuSelect = menuService.menuListSelect(mozip.get().getId());
            if(menuSelect.isEmpty())
                return ResponseEntity.badRequest().body("메뉴가 조회되지 않습니다.");

            return ResponseEntity.ok(menuSelect);
        }

        return ResponseEntity.badRequest().body("채팅방이 조회되지 않았습니다.");
    }
}
