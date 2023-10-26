package backend.codebackend.controller;

import backend.codebackend.domain.Basket;
import backend.codebackend.service.BasketService;
import backend.codebackend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketController {
    private final BasketService basketService;
    private final MemberService memberService;
    @PostMapping("/basket")
    public List<Basket> listBasketItems(@PathVariable Long chatroom_id) {
        List<Basket> basketItems = basketService.findAll(chatroom_id);
        return basketItems;
    }

    @PostMapping("/basket/add")
    public boolean addItemToBasket(Long chatroom_id, String menuName, String menuPrice, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        //menuPrice : "10,000원" 에서 숫자 이외의 모든 문자 제거 후 int 타입 변수에 대입
        basketService.addItemToBasket(chatroom_id, menuName
                , Integer.parseInt(menuPrice.replaceAll("[^0-9]", ""))
                , memberService.findLoginId(String.valueOf(session.getAttribute("memberId")))
                    .get()
                    .getNickname()
                );

        return true;
    }
}
