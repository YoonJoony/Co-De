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
}
