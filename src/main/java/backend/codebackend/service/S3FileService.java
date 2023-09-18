package backend.codebackend.service;

import backend.codebackend.dto.FileUploadDto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3FileService implements FileService{

    //AmazonS3 주입받기
    private final AmazonS3 amazonS3;

    //S3 bucket 이름
    //application.properties에 넣어 둔 내용이 들어오게 된다.
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //S3 base URL
    @Value("${cloud.aws.s3.bucket.url}")
    private String baseUrl;

    //MultipartFile과 transcation, roomId를 전달 받는다.
    //이때 transcation은 파일 이름 중복 방지를 위한 UUID를 의미한다.
    @Override
    public FileUploadDto uploadFile(MultipartFile file, String transaction, String roomId) {
        /*  MultipartFile : HTTP 요청에서 전송된 파일을 나타내는 인터페이스 입니다.
                            일반적으로 클라이언트에서 서버로 파일을 업로드할 때 사용됨.    */

        try{
            String filename = file.getOriginalFilename(); // 파일원본 이름
        /*  getOriginalFilename() : 클라이언트에서 전송한 파일의 원래 이름을 반환한다.
                                    서버에서는 보안상 파일의 이름을 변경하는 경우가 많기 때문.    */

            String key = roomId+"/"+transaction+"/"+filename; // S3 파일 경로
            log.info("\n\n 파일 경로 : " + key);

            // 매개변수로 넘어온 multipartFile 을 File 객체로 변환 시켜서 저장하기 위한 메서드
            File convertedFile = convertMultipartFileToFile(file, transaction + filename);

            // 아마존 S3 에 파일 업로드를 위해 사용하는 TransferManagerBuilder
            TransferManager transferManager = TransferManagerBuilder //TransferManagerBuiler : TransferManager 객체를 생성하기 위한 빌더 클래스
                    .standard() //빌더 객체 생성
                    .withS3Client(amazonS3) // Amazon S3 클라이언트 객체 지정
                    .build(); // 객체 생성

            // bucket 에 key 와 converedFile 을 이용해서 파일 업로드
            Upload upload = transferManager.upload(bucket, key, convertedFile);
            //upload(버킷이름, 업로드할 파일의 경로(이름), 업로드 할 파일)
            upload.waitForUploadResult(); //업로드가 완료 될 때까지 대기한다.

            // 변환된 File 객체 삭제
            removeFile(convertedFile);

            // uploadDTO 객체 빌드
            FileUploadDto uploadReq = FileUploadDto.builder()
                    .transaction(transaction)
                    .chatRoom(roomId)
                    .originFileName(filename)
                    .fileDir(key)
                    .s3DataUrl(baseUrl+"/"+key)
                    .build();

            // uploadDTO 객체 리턴
            return uploadReq;

        } catch (Exception e) {
            log.error("fileUploadException {}", e.getMessage());
            return null;
        }
    }

    //S3에서 파일 또는 디렉토리를 삭제하는 메소드.
    //path 매개변수로 전달된 경로에 있는 파일 또는 디렉토리를 삭제
    @Override
    public void deleteFileDir(String path) {
        /*  amazonS3.listObject(bucket, path) 는 S3 에서 지정된 버킷과 경로에 있는 객체 목록을 가져오는 메소드
            S3ObjectSummaries()는 객체 목록을 반환한다.

            for 에서는 getSummaries() 메소드로 가져온 S3ObjectSummary 객체의 목록을 반복하며
            amazonS3.deleteObject() 메소드로 객체를 삭제한다.
         */
        for (S3ObjectSummary summary : amazonS3.listObjects(bucket, path).getObjectSummaries()) {
            amazonS3.deleteObject(bucket, summary.getKey());
        }
    }

    // byte 배열 타입을 return 한다.

    @Override
    public ResponseEntity<byte[]> getObject(String fileDir, String fileName) throws IOException {
        // bucket 와 fileDir 을 사용해서 S3 에 있는 객체 - object - 를 가져온다.
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, fileDir));

        // object 를 S3ObjectInputStream 형태로 변환한다.
        S3ObjectInputStream objectInputStream = object.getObjectContent();

        // 이후 다시 byte 배열 형태로 변환한다.
        // 아마도 파일 다운로드를 위해서는 byte 형태로 변환할 필요가 있어서 그런듯하다
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        // 여기는 httpHeader 에 파일 다운로드 요청을 하기 위한내용
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        /*
            HttpHeader 클래스는 HTTP 요청 또는 응답의 헤더 저오를 나타내느 클래스.
                            위 객체를 사용하여 HTTP요청 또는 응답의 헤더정보를 설정할 수 있음
            setContentType을 이용하여 컨텐츠 타입을 APPLICATION_OCTET_STREAM으로 설정.
         */


        // 지정된 fileName 으로 파일이 다운로드 된다.
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        //컨텐츠의 디스포지션 헤더를 설정한다. (파일이 다운될 때 브라우저에서 보여줄 파일 이름, 실제 파일 이름)

        log.info("HttpHeader : [{}]", httpHeaders);

        // 최종적으로 ResponseEntity 객체를 리턴하는데
        // --> ResponseEntity 란?
        // ResponseEntity 는 사용자의 httpRequest 에 대한 응답 테이터를 포함하는 클래스이다.
        // 단순히 body 에 데이터를 포함하는 것이 아니라, header 와 httpStatus 까지 넣어 줄 수 있다.
        // 이를 통해서 header 에 따라서 다른 동작을 가능하게 할 수 있다 => 파일 다운로드!!

        // 나는 object가 변환된 byte 데이터, httpHeader 와 HttpStatus 가 포함된다.
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
}
