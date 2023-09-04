package backend.codebackend.repository;

import backend.codebackend.domain.Chat;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository{

    private final EntityManager em;

    @Override
    public void save(Chat chat) {
        em.persist(chat);
    }
}
