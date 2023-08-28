package backend.codebackend.repository;

import backend.codebackend.domain.ChatUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor //생성자 주입으로 생성자 생략
public class JpaChatUserRepository implements ChatUserRepository{
    private final EntityManager em; //jpa를 라이브러리로 받으면 스프링 부트가 자동으로 EntityManager를 생성해줌


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
}

