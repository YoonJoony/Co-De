package backend.codebackend.repository;

import backend.codebackend.domain.ChatUser;

import java.util.ArrayList;
import java.util.List;

public interface ChatUserRepository {
    boolean addUser(ChatUser chatUser);

    void deleteUser(Long id, String nickname);
    ArrayList<String> getUserList(Long id);
}
