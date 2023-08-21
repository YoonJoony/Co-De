package backend.codebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    //메시지 타입 : 입장, 채팅
    //메시지 타입에 따라서 동작하는 구조가 달라진ㄷ
    //입장과 퇴장 ENTER과 LEAVE의 경우 입장/퇴장 이벤트 처리가 실행되고,
    //TALK는 말 그대로 내용이 해당하는 채팅방을 SUB 하고 있는 모든 클라이언트에게 전달된다.
    public enum MessageTYpe{
        ENTER, LEAVE, TALK,
    }

    private MessageTYpe type; //메시지 타입
    private Long id; //방 번호
    private String sender; //채팅을 보낸 사람
    private String message; //메시지

    /* 파일 업로드 관련 변수 */
    private String s3DataUrl; // 파일 업로드 url
    private String fileName; // 파일이름
    private String fileDir; // s3 파일 경로
}
