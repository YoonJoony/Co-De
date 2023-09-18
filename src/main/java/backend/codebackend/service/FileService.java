package backend.codebackend.service;

import backend.codebackend.dto.FileUploadDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public interface FileService {
    // 파일 업로드를 위한 메서드 선언
    FileUploadDto uploadFile(MultipartFile file, String transaction, String roomId);

    // 현재 방에 업로드된 모든 파일 삭제 메서드
    void deleteFileDir(String path);

    // 컨트롤러에서 받아온 multipartFile 을 File 로 변환시켜서 저장하기 위한 메서드
    default File convertMultipartFileToFile(MultipartFile mfile, String tmpPath) throws IOException {
        File file = new File(tmpPath);
        //tmpPath 라는 이름을 가진 file 객체 생성

        if (file.createNewFile()) { //file을 생성.
            try (FileOutputStream fos = new FileOutputStream(file)) {
                //FileOutputStream : 파일에 바이트 단위로 데이터를 출력하는 메소드.
                //FileOutputStream(file) 로 file을 넣으면 해당 file에 바이트 단위의 데이터를 씌울 수 있다.
                fos.write(mfile.getBytes());
                //전달받은 파일을 바이트 단위로 추출하여 file에 파일 데이터가 씌워진다.
            }
            return file;
        }
        throw new IOException();
    }

    // 파일 삭제
    default void removeFile(File file){
        file.delete();
    }

    ResponseEntity<byte[]> getObject(String fileDir, String fileName) throws IOException;
}
