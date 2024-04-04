package backend.codebackend.repository;

import backend.codebackend.domain.Basket;
import backend.codebackend.dto.TotalPrice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@PersistenceContext // em.find() 메소드를 사용하여 Basket 클래스의 id가 8인 튜플을 조회할 수 있음
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
            String updateQuery = "update Basket m set m.quantity = m.quantity + 1 where m.product_name = :product_name and m.nickname = :nickname";
            em.createQuery(updateQuery)
                    .setParameter("product_name", product_name)
                    .setParameter("nickname", nickname)
                    .executeUpdate();
        }

        return basket;
    }

    @Override
    public Basket save(Basket basket) {
        em.persist(basket);
        return basket;
    }

    // 수량 + 1
    @Override
    public void plusItemCnt(Long menuId, String updateQuantityNickName) {
        String plusItem =
                "update Basket m set m.quantity = m.quantity + 1 where m.id = :menuId " +
                        "and m.nickname = :updateQuantityNickName";

        em.createQuery(plusItem)
                .setParameter("menuId",  menuId)
                .setParameter("updateQuantityNickName", updateQuantityNickName)
                .executeUpdate();   //executeUpdate 메서드는 업데이트된 엔티티 수를 반환
    }

    // 수량 - 1
    @Override
    public void minusItemCnt(Long menuId, String updateQuantityNickName) {
        String minusItem =
                "update Basket m set m.quantity = m.quantity - 1 where m.id = :menuId " +
                        "and m.nickname = :updateQuantityNickName";

        em.createQuery(minusItem)
                .setParameter("menuId",  menuId)
                .setParameter("updateQuantityNickName", updateQuantityNickName)
                .executeUpdate();   //executeUpdate 메서드는 업데이트된 엔티티 수를 반환
    }

    //장바구니 항목 중 해당하는 항목 한 개를 삭제(선택한 것만)
    @Override
    public void deleteByMenu(Long menuId) {
        String deleteQuery = "delete from Basket m where m.id = :menuId";
        em.createQuery(deleteQuery)
                .setParameter("menuId", menuId)
                .executeUpdate();
    }

    //다른 사람이 추가한 최신 메뉴를 받음 (STOMP)
    @Override
    public Basket addItemToBasketReceive(String nickname) {
        String jpql = "SELECT m FROM Basket m WHERE m.nickname = :nickname ORDER BY m.updated_at DESC";
        TypedQuery<Basket> query = em.createQuery(jpql, Basket.class);
        query.setParameter("nickname", nickname);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    //닉네임에 해당하는 장바구니 메뉴 전부 조회
    @Override
    public List<Basket> personalBasket(String nickname) {
        String jpql = "SELECT m FROM Basket m WHERE m.nickname = :nickname";
        TypedQuery<Basket> query = em.createQuery(jpql, Basket.class);
        query.setParameter("nickname", nickname);

        return query.getResultList();
    }

    @Override
    public List<TotalPrice> getTotalPrice(Long roomId) {
        String jpql = "SELECT b.nickname, SUM(b.price * b.quantity) " +
                "FROM Basket b WHERE b.chatroom_id = :roomId GROUP BY b.nickname";
        Query query = em.createQuery(jpql);
        query.setParameter("roomId", roomId);

        List<Object[]> results = query.getResultList();
        List<TotalPrice> totalPriceList = new ArrayList<>();

        for (Object[] result : results) {
            String nickname = (String) result[0];
            int totalPrice = ((Number) result[1]).intValue();

            TotalPrice totalPriceObj = TotalPrice.builder()
                    .username(nickname)
                    .totalPrice(totalPrice)
                    .build();
            totalPriceList.add(totalPriceObj);
        }

        return totalPriceList;
    }

    //장바구니에 있는 항목 전체를 한꺼번에 삭제(모두 지우기)
    @Override
    public void deleteByAllMenu(Long roomId) {
        String deleteQuery = "delete from Basket m where m.chatroom_id = :roomId";
        em.createQuery(deleteQuery)
                .setParameter("roomId", roomId)
                .executeUpdate();
    }

    @Override
    public Basket findBasketMenu(Long id) {
        return em.find(Basket.class, id);
    }
}
