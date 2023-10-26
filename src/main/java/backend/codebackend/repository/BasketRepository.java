package backend.codebackend.repository;


import backend.codebackend.domain.Basket;

import java.util.List;
import java.util.Optional;

public interface BasketRepository {
    List<Basket> findAll(Long id);
    Optional<Basket> duplicateBasketItem(Long idd, String product_name, int price, String nickname);

    void save(Basket basket);
    void deleteBasket(Long nickname);

    void deleteByItem(Long basketItemId);

    void plusItemCnt(Long basketItemId);

    void minusItemCnt(Long basketItemId);
}


