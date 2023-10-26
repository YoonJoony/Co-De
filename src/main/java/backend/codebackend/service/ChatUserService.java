package backend.codebackend.service;

import backend.codebackend.domain.ChatUser;
import backend.codebackend.repository.ChatUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class ChatUserService {
    private final ChatUserRepository chatUserRepository;
    private final MozipService mozipService;

    public boolean addUser(Long id, String nickname) {
        ChatUser chatUser = ChatUser.builder()
                .id(id)
                .nickname(nickname)
                .build();

        if(chatUserRepository.addUser(chatUser)) {
            return true;
        }
        return false;
    }

    public boolean isDuplicateName(Long id, String nickname) {
        if(chatUserRepository.isDuplicateName(id, nickname))
            return true;
        return false;
    }

    public void deleteUser(Long id, String nickname) {
        chatUserRepository.deleteUser(id, nickname);
    }

    public ArrayList<String> getUserList(Long id) {
        ArrayList<String> result = chatUserRepository.getUserList(id);
        return result;
    }

    public Long findMemberRoomId(String nickname) {
        Optional<Long> roomId = chatUserRepository.findRoomId(nickname);
        if (roomId.isEmpty())
            return null;

        return roomId.get();
    }

}
