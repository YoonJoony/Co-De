package backend.codebackend.service;

import backend.codebackend.domain.ChatUser;
import backend.codebackend.domain.Mozip;
import backend.codebackend.repository.ChatUserRepository;
import backend.codebackend.repository.MozipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor //생성자 주입. 번거롭게 생성자를 생성해서 객체를 주입받지 않아도 됨.
public class ChatUserService {
    private final ChatUserRepository chatUserRepository;
    private final MozipRepository mozipRepository;
    private final MozipService mozipService;

    public boolean addUser(Long id, String nickname) {
        Optional<Mozip> room = mozipRepository.findById(id);
        ChatUser chatUser;
        if(room.isPresent()) {
            String hostNickname = room.get().getNickname();

            //호스트일 경우
            if(Objects.equals(nickname, hostNickname)) {
                chatUser = ChatUser.builder()
                        .id(id)
                        .nickname(nickname)
                        .host(1)
                        .build();
            } else { //호스트가 아닐 경우
                chatUser = ChatUser.builder()
                        .id(id)
                        .nickname(nickname)
                        .host(0)
                        .build();
            }

            return chatUserRepository.addUser(chatUser);
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
