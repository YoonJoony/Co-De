package backend.codebackend.repository;

import backend.codebackend.domain.Basket;
import backend.codebackend.domain.ChatUser;
import backend.codebackend.service.ChatUserService;
import backend.codebackend.service.MozipService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor //생성자 주입으로 생성자 생략
public class JpaChatUserRepository implements ChatUserRepository{
    private final EntityManager em; //jpa를 라이브러리로 받으면 스프링 부트가 자동으로 EntityManager를 생성해줌
    private final MozipService mozipService;

    @Override
    public boolean addUser(ChatUser chatUser) {
        List<String> result = em.createQuery("select m.nickname from ChatUser m where m.nickname = :nickname and m.id = :id", String.class)
                .setParameter("nickname", chatUser.getNickname())
                .setParameter("id", chatUser.getId())
                .getResultList();

        if(result.isEmpty()){
            em.persist(chatUser);
            return true;
        }
        return false;
    }

    //기존에 접속한 유저인 경우
    @Override
    public boolean isDuplicateName(Long id, String nickname) {
        List<String> result = em.createQuery("select m.nickname from ChatUser m where m.nickname = :nickname and m.id = :id", String.class)
                .setParameter("nickname", nickname)
                .setParameter("id", id)
                .getResultList();
        if(result.isEmpty()){ //기존 유저가 아닌 경우
            return true;
        }
        return false;
    }

    @Override
    public void deleteUser(Long id, String nickname) {
        Query query = em.createQuery("delete from chat_user where id = :id and nickname = :nickname");
        query.setParameter("id", id);
        query.setParameter("nickname", nickname);
        query.executeUpdate();
    }

    @Override
    public ArrayList<String> getUserList(Long id) {
        TypedQuery<String> typedQuery = em.createQuery("select m.nickname from ChatUser m where m.id = :id", String.class);
        typedQuery.setParameter("id", id); // id 값을 설정

        return (ArrayList<String>) typedQuery.getResultList();
    }

    @Override
    public boolean is_host(Long id, String nickname) {
        List<String> result = em.createQuery("select m.nickname from Mozip m where m.nickname = :nickname and m.id = :id ", String.class)
                .setParameter("nickname", nickname)
                .setParameter("id", id)
                .getResultList();

        if (result.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public String findHost(Long id) {
        TypedQuery<String> query = em.createQuery(
                "SELECT m.nickname FROM ChatUser m WHERE m.id = :id AND m.host = 1", String.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }



    @Override
    public Optional<Long> findRoomId(String nickname) {
        List<Long> result = em.createQuery("select m.id from ChatUser m where m.nickname = :nickname", Long.class)
                .setParameter("nickname", nickname)
                .getResultList();

        if (result.isEmpty()){
            return null;
        }
        return result.stream().findAny();
    }

    //접속한 방이 있을 경우
    @Override
    public boolean isDuplicateRoom(Long id, String nickname) {
        try {
            String jpql = "SELECT m FROM ChatUser m WHERE m.nickname = :nickname";
            TypedQuery<ChatUser> query = em.createQuery(jpql, ChatUser.class);
            query.setParameter("nickname", nickname);
            ChatUser chatUser;

            try {
                chatUser = query.getSingleResult();
            } catch (NoResultException e) { //참여한 방이 없을 경우
                if(mozipService.chkRoomUserCnt(id)) {
                    mozipService.plusUserCnt(id);
                    System.out.println("현재 인원 : " + mozipService.findRoomById(id).get().getUsercount());
                    System.out.println("신입이군요");
                    return true;
                }
                return false;
            }
            
            //접속한 방에 들어갔을 경우 true, 아닐경우 false
            return Objects.equals(chatUser.getId(), id);
        } finally {
            em.close();
        }
    }
}

