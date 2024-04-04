package backend.codebackend.repository;


import backend.codebackend.domain.Basket;
import backend.codebackend.dto.TotalPrice;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BasketRepository {
    List<Basket> findAll(Long id);
    Optional<Basket> duplicateBasketItem(Long idd, String product_name, int price, String nickname);

    Basket save(Basket basket);
    void plusItemCnt(Long menuId, String updateQuantityNickName);

    void minusItemCnt(Long menuId, String updateQuantityNickName);
    void deleteByAllMenu(Long menuId);

    void deleteByMenu(Long menuId);
    Basket findBasketMenu(Long menuId);
    Basket addItemToBasketReceive(String nickname);
    List<Basket> personalBasket(String nickname);
    List<TotalPrice> getTotalPrice(Long roomId);
}


