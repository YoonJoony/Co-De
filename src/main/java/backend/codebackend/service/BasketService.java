package backend.codebackend.service;

import backend.codebackend.domain.Basket;
import backend.codebackend.domain.Mozip;
import backend.codebackend.dto.TotalPrice;
import backend.codebackend.repository.BasketRepository;
import backend.codebackend.repository.ChatRepository;
import backend.codebackend.repository.MozipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;

    // 채팅방 번호로 각 채팅방의 장바구니 조회
    public List<Basket> findAll(Long id) {
        return basketRepository.findAll(id);
    }

    //장바구니에 항목 추가
    public Basket addItemToBasket(Long chatroom_id, String product_name, int price, String nickname) {
        //이미 장바구니에 존재하는 항목인지 확인
        if (basketRepository.duplicateBasketItem(chatroom_id, product_name, price, nickname).isPresent()) {      //항목(음식)이 비어있지 않다면 -> 이미 담아서 존재함.
            System.out.println("기존 메뉴 수량 추가");
        } else {  //장바구니에 존재하지 않는 항목이면 추가
            Basket newItem = Basket.builder()
                    .chatroom_id(chatroom_id)
                    .product_name(product_name)
                    .price(price)
                    .quantity(1)
                    .nickname(nickname)
                    .build();
            // 업데이트된 장바구니를 저장
            return basketRepository.save(newItem);
        }
        System.out.println("장바구니애 저장 안됨.");
        return null;
    }

    //장바구니 항목 수량 증가(+)
    public void plusItemCnt(Long menuId, String updateQuantityNickName) {

        basketRepository.plusItemCnt(menuId, updateQuantityNickName);
    }

    //장바구니 항목 수량 감소(-)
    public void minusItemCnt(Long menuId, String updateQuantityNickName) {
        basketRepository.minusItemCnt(menuId, updateQuantityNickName);
    }

    // 장바구니 항목 한 개 삭제
    public void deleteByMenu(Long menuId) {
        basketRepository.deleteByMenu(menuId);
    }

    // 장바구니 항목 전체 삭제(전부 다 삭제)
    public void deleteBasket(Long menuId) {
        basketRepository.deleteByAllMenu(menuId);
    }


    //메뉴 기본키에 해당하는 장바구니 메뉴 조회
    public Basket findBasketMenu(Long id) {
        return basketRepository.findBasketMenu(id);
    }


    //닉네임에 해당하는 장바구니 메뉴 투플 중 가장 최근 투플
    public Basket addItemToBasketReceive(String nickname) {
//        if(basketRepository.duplicateBasketItem(chatroom_id, product_name, price, nickname).isPresent())
        return basketRepository.addItemToBasketReceive(nickname);
    }

    //닉네임에 해당하는 장바구니 전부 조회
    public List<Basket> personalBasket(String nickname) {
        return basketRepository.personalBasket(nickname);
    }

    //총 장바구니 금액, 닉네임 리턴
    public List<TotalPrice> getTotalPrice(Long roomId) {
        return basketRepository.getTotalPrice(roomId);
    }

}

