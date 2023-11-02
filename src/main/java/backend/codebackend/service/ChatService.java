package backend.codebackend.service;

import backend.codebackend.domain.Chat;
import backend.codebackend.dto.ChatDTO;
import backend.codebackend.repository.ChatRepository;
import backend.codebackend.repository.ChatUserRepository;
import backend.codebackend.repository.JpaChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;

    public Chat save(ChatDTO chatDTO) {
        Chat chat = Chat.builder()
                .chatId(UUID.randomUUID().toString())
                .id(chatDTO.getId())
                .sender(chatDTO.getSender())
                .message(chatDTO.getMessage())
                .type(String.valueOf(chatDTO.getType()))
                .build();
        chatRepository.save(chat);
        return chat;
    }

    //채팅 친 시간 조회
    public void timestamp(Chat chat) {
        LocalDateTime dateTime = chat.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = dateTime.format(formatter);
        System.out.println(time);
    }

    //채팅 참가 시 호스트 여부 확인: 채팅에 참가하는 사용자가 호스트인지 아닌지를 확인해야 함
    //이를 위해 채팅 참가 로직에서 현재 사용자가 해당 모집글의 호스트인지 여부를 확인하고, 이 정보를 사용하여 추방 버튼을 표시하거나 숨김
    public boolean isCurrentUserHost(Long id, String nickname){

        return chatUserRepository.is_host(id, nickname);
    }
}
