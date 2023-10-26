package backend.codebackend.repository;

import backend.codebackend.domain.Basket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaBasketRepository implements BasketRepository {
    private final EntityManager em;

    @Override
    public List<Basket> findAll(Long chatroom_id) {
        List<Basket> result = em.createQuery("select m from Basket m where m.chatroom_id = :chatroom_id", Basket.class)
                .setParameter("chatroom_id", chatroom_id)
                .getResultList();
        return result;
    }

    @Override
    public Optional<Basket> duplicateBasketItem(Long chatroom_id, String product_name, int price, String nickname) {
        List<Basket> result = em.createQuery("select m from Basket m where m.chatroom_id = :chatroom_id and m.product_name = :product_name and m.price = :price and m.nickname = :nickname", Basket.class)
                .setParameter("chatroom_id", chatroom_id)
                .setParameter("product_name", product_name)
                .setParameter("price", price)
                .setParameter("nickname", nickname)
                .getResultList();

        Optional<Basket> basket = result.stream().findAny();

        if(basket.isPresent()) {
            String updateQuery = "update Basket m set m.quantity = m.quantity + 1 where m.product_name = :product_name";
            em.createQuery(updateQuery)
                    .setParameter("product_name", product_name)
                    .executeUpdate();
        }

        return basket;
    }

    @Override
    public void save(Basket basket) {
        em.persist(basket);
    }

    //장바구니에 있는 항목 전체를 한꺼번에 삭제(모두 지우기)
    @Override
    public void deleteBasket(Long nickname) {
        String deleteQuery = "delete from Basket m where m.nickname = :nickname";
        em.createQuery(deleteQuery)
                .setParameter("nickname", nickname)
                .executeUpdate();
    }
    //장바구니 항목 중 해당하는 항목 한 개를 삭제(선택한 것만)
    @Override
    public void deleteByItem(Long basketItemId) {
        String deleteQuery = "delete from Basket m where m.basket.id = :basketItemId";
        em.createQuery(deleteQuery)
                .setParameter("basketItemId", basketItemId)
                .executeUpdate();
    }

    // 수량 + 1
    @Override
    public void plusItemCnt(Long basketItemId) {
        String plusItem = "update basket m set m.quantity = m.quantity + 1 where m.id = :basketItemId";
        em.createQuery(plusItem)
                .setParameter("basketItemId", basketItemId)
                .executeUpdate();   //executeUpdate 메서드는 업데이트된 엔티티 수를 반환
    }
    // 수량 - 1
    @Override
    public void minusItemCnt(Long basketItemId) {
        String minusItem = "update basket m set m.quantity = m.quantity - 1 where m.id = :basketItemId";
        em.createQuery(minusItem)
                .setParameter("basketItemId", basketItemId)
                .executeUpdate();
    }
}
