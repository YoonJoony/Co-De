package backend.codebackend;

import backend.codebackend.controller.AccountController;
import backend.codebackend.domain.Chat;
import backend.codebackend.repository.*;
import backend.codebackend.service.*;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.time.LocalDateTime;

//빈을 직접 찾아서 등록하는거라 따로 서비스나 저장소에서 @service... @repo.. 안해줘도 됨

@Configuration
@EnableWebSocketMessageBroker
@EnableAsync
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
        return new ChatUserService(chatUserRepository(), mozipRepository(), mozipService());
    }

    @Bean
    public ChatService chatService() {
        return new ChatService(chatRepository(), chatUserRepository());
    }
    @Bean
    public AccountService accountService() {
        return new AccountService(accountRepository(), encoder());
    }
    @Bean
    public BasketService basketService() {
        return new BasketService(basketRepository());
    }
    @Bean
    public SystemAccountService systemAccountService() {
        return new SystemAccountService(em, systemAccountRepository(), chatUserRepository(), accountService(), encoder(), memberService());
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

    @Bean
    public ChatRepository chatRepository() {
        return new JpaChatRepository(em);
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JpaAccountRepository(em);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public BasketRepository basketRepository() {
        return new JpaBasketRepository(em);
    }
    @Bean
    public SystemAccountRepository systemAccountRepository() {
        return new JpaSystemAccountRepository(em);
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

        /*
        메시지 브로커를 구성하는데 사용됨. 이 메소드를 사용하여 메시지 브로커를 구성하면 클라이언트에서 전송된 메시지를 처리할 수 있다.
        enableSimpleBroker() 메시지 브로커가 구독할 주제를 정함
        setApplicationDestinationPrefixes 클라이언트가 메시지를 보낼 때 사용될 주소 접두사를 정함.

         */
    }
}
