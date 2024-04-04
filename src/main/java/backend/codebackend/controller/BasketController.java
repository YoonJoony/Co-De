package backend.codebackend.controller;

import backend.codebackend.domain.Basket;
import backend.codebackend.domain.Member;
import backend.codebackend.dto.TotalPrice;
import backend.codebackend.service.BasketService;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.MozipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketController {
    private final BasketService basketService;
    private final MemberService memberService;
    private final MozipService mozipService;
    @PostMapping("/basket")
    public List<Basket> listBasketItems(@PathVariable Long chatroom_id) {
        List<Basket> basketItems = basketService.findAll(chatroom_id);
        return basketItems;
    }

    @PostMapping("/basket/add")
    public ResponseEntity<?> addItemToBasket(Long chatroom_id, String menuName, String menuPrice, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("세션이 없습니다.");
            return ResponseEntity.badRequest().build();
        }

        //정산 상태 확인
        if(mozipService.mozipStatus(chatroom_id)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "정산 시작된 상태입니다!"));
        }

        Optional<Member> member = memberService.findLoginId(String.valueOf(session.getAttribute("memberId")));
        //menuPrice : "10,000원" 에서 숫자 이외의 모든 문자 제거 후 int 타입 변수에 대입
        //value : member 값이 null이 아닐 경우 객체, orElse : member 값이 null인 경우 null
        Basket basket = member.map(value -> basketService.addItemToBasket(chatroom_id, menuName
                , Integer.parseInt(menuPrice.replaceAll("[^0-9]", ""))
                , value.getNickname()
        )).orElse(null);

        if (basket == null) {
            return null;
        } else {
            return ResponseEntity.ok(basket);
        }
    }

    //다른 사람이 추가한 최신 메뉴를 받음 (STOMP)
    @PostMapping("/basket/add/recv")
    public Basket addItemToBasketReceive(String nickname) {
        return basketService.addItemToBasketReceive(nickname);
    }

    //내가 담은 장바구니 조회
    @GetMapping("/basket/personal")
    public ResponseEntity<?> personalBasket(HttpSession session) {
        Optional<Member> member = memberService.findLoginId(String.valueOf(session.getAttribute("memberId")));
        if (member.isPresent()) {
            return ResponseEntity.ok(basketService.personalBasket(member.get().getNickname()));
        } else {
            return ResponseEntity.badRequest().body("세션 오류");
        }
    }

    //채팅방 장바구니 조회
    @GetMapping("/chat/basket")
    @ResponseBody
    public List<Basket> basket(Long roomId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        List<Basket> basket = basketService.findAll(roomId);

        if(basket == null)
            log.info("장바구니가 조회되지 않음. ");

        return basket;
    }

    //채팅방 장바구니 메뉴 수량 수정

    //증가 버튼
    @PostMapping("/chat/basket/plusQuantity")
    @ResponseBody
    public ResponseEntity<?>  plusQuantity(Long chatroom_id, Long menuId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            log.info("세션이 없습니다.");
            return ResponseEntity.badRequest().build();
        }
        
        //정산 상태 확인
        if(mozipService.mozipStatus(chatroom_id)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "정산 시작된 상태입니다!"));
        }

        //HTML의 menuId를 수정하여 접근하는 유저를 방지하기 위해 장바구니 실제로 담은 사람이랑 로그인 유저랑 비교한다.
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname();

        if(!basketService.findBasketMenu(menuId).getNickname().equals(nickname)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "본인의 메뉴만 수정 할 수 있습니다."));
        }

        basketService.plusItemCnt(menuId, nickname);

        return ResponseEntity.ok("수량 수정");
    }

    //감소 버튼
    @PostMapping("/chat/basket/minusQuantity")
    @ResponseBody
    public ResponseEntity<?>  minusQuantity(Long chatroom_id, Long menuId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        //정산 상태 확인
        if(mozipService.mozipStatus(chatroom_id)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "정산 시작된 상태입니다!"));
        }

        //HTML의 menuId를 수정하여 접근하는 유저를 방지하기 위해 장바구니 실제로 담은 사람이랑 로그인 유저랑 비교한다.
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname();

        if(!basketService.findBasketMenu(menuId).getNickname().equals(nickname)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "본인의 메뉴만 수정 할 수 있습니다."));
        }

        basketService.minusItemCnt(menuId, nickname);
        return  ResponseEntity.ok("- 수량 수정");
    }

    //메뉴 삭제 버튼
    @PostMapping("/chat/basket/deleteByMenu")
    @ResponseBody
    public ResponseEntity<?> deleteByMenu(Long chatroom_id, Long menuId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("세션이 없습니다.");
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "세션이 없습니다. 다시 로그인 해주세요."));
        }

        //정산 상태 확인
        if(mozipService.mozipStatus(chatroom_id)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "정산 시작된 상태입니다!"));
        }
        
        //HTML의 menuId를 수정하여 접근하는 유저를 방지하기 위해 장바구니 실제로 담은 사람이랑 로그인 유저랑 비교한다.
        String nickname = memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).get().getNickname();
        if(!basketService.findBasketMenu(menuId).getNickname().equals(nickname)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "본인의 메뉴만 삭제 할 수 있습니다."));
        }

        basketService.deleteByMenu(menuId);
        return ResponseEntity.ok("장바구니 삭제");
    }

    //장바구니 총 금액 뷰
    @GetMapping("/chat/basket/totalPrice")
    @ResponseBody
    public List<TotalPrice> getTotalPrice(Long id) {

        return basketService.getTotalPrice(id);
    }
}
