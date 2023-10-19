package backend.codebackend.service;

import backend.codebackend.domain.Basket;
import backend.codebackend.domain.Mozip;
import backend.codebackend.repository.BasketRepository;
import backend.codebackend.repository.ChatRepository;
import backend.codebackend.repository.MozipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ChatRepository chatRepository;
    private final MozipRepository mozipRepository;

    // 채팅방 번호로 각 채팅방의 장바구니 조회
    public List<Basket> findAll(Long id) {
        return basketRepository.findAll(id);
    }

    //장바구니에 항목 추가
    public void addItemToBasket(Long id, String productName, int price, String nickname) {
        List<Basket> existingBasket = basketRepository.findAll(id);  //장바구니(id)를 찾음

        //이미 장바구니에 존재하는 항목인지 확인
        if (!existingBasket.isEmpty()) {      //항목(음식)이 비어있지 않다면 -> 이미 담아서 존재함.
            for (Basket item : existingBasket) { // 메뉴이름 & 가격을 비교하여 이미 존재하는 항목인지 확인
                if (item.getProductName().equals(productName) && item.getNickname().equals(nickname)
                        && item.getPrice() == price) {
                    item.setQuantity(item.getQuantity() + 1);   // 존재하면 개수를 증가시킴
                    break;
                }
            }
        } else {  //장바구니에 존재하지 않는 항목이면 추가
            Basket newItem = Basket.builder()
                    .id(id)
                    .productName(productName)
                    .price(price)
                    .quantity(1)
                    .nickname(nickname)
                    .build();
            // 업데이트된 장바구니를 저장
            basketRepository.save(newItem);
        }

    }
    // 장바구니 항목 전체 삭제(전부 다 삭제)
    public void deleteBasket(Long nickname) {
        basketRepository.deleteBasket(nickname);
    }
    // 장바구니 항목 한 개 삭제
    public void deleteByItem(Long basketItemId) {
        basketRepository.deleteByItem(basketItemId);
    }

    //장바구니 항목 수량 증가(+)
    public void plusItemCnt(Long basketItemId) {
        basketRepository.plusItemCnt(basketItemId);
    }
    //장바구니 항목 수량 감소(-)
    public void minusItemCnt(Long basketItemId) {
        basketRepository.minusItemCnt(basketItemId);
    }



}

