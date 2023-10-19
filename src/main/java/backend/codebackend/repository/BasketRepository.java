package backend.codebackend.repository;


import backend.codebackend.domain.Basket;

import java.util.List;

public interface BasketRepository {
    List<Basket> findAll(Long id);
    void save(Basket basket);

    void deleteBasket(Long nickname);

    void deleteByItem(Long basketItemId);

    void plusItemCnt(Long basketItemId);

    void minusItemCnt(Long basketItemId);
}


