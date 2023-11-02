package backend.codebackend.repository;

import backend.codebackend.domain.ChatUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ChatUserRepository {
    boolean addUser(ChatUser chatUser);

    boolean isDuplicateName(Long id, String nickname);

    void deleteUser(Long id, String nickname);
    ArrayList<String> getUserList(Long id);

    boolean is_host(Long id, String nickname);
    String findHost(Long id);

    Optional<Long> findRoomId(String nickname);
}
