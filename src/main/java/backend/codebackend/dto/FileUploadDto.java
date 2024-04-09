package backend.codebackend.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
public class FileUploadDto {
    
    private MultipartFile file;
    
    private String originFileName; //원본 파일 이름
    
    private String transaction; //UUID를 활용한 랜덤한 파일위치
    
    private String chatRoom; //파일이 올라간 채팅방 ID
    
    private String s3DataUrl; //파일 링크
    
    private String fileDir; //S3 파일 경로
}
