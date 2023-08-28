package backend.codebackend;

import backend.codebackend.repository.*;
import backend.codebackend.service.*;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//빈을 직접 찾아서 등록하는거라 따로 서비스나 저장소에서 @service... @repo.. 안해줘도 됨


@Configuration
@EnableWebSocketMessageBroker
public class SpringConfig implements WebSocketMessageBrokerConfigurer {


    private final EntityManager em;

    private final AmazonS3 amazonS3;


    public SpringConfig(EntityManager em, AmazonS3 amazonS3) {
        this.em = em;
        this.amazonS3 = amazonS3;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MozipService mozipService() {
        return new MozipService(mozipRepository(), memberRepository());
    }

    @Bean
    public FileService fileService() {
        return new S3FileService(amazonS3);
    }

    @Bean
    public ChatUserService chatUserService() {
        return new ChatUserService(chatUserRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public MozipRepository mozipRepository() {
        return new JpaMozipRepository(em);
    }

    @Bean
    public ChatUserRepository chatUserRepository() {
        return new JpaChatUserRepository(em);
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 주소 url => /ws-stomp
        registry.addEndpoint("/ws-stomp") // 연결될 엔드포인트
                .withSockJS(); // SocketJS 를 연결한다는 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
        registry.enableSimpleBroker("/sub");

        // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
