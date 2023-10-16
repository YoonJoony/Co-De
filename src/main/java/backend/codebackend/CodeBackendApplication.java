package backend.codebackend;

import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ServletComponentScan
@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //스프링 시큐리티에서 자동으로 생성하는 로그인페이지 제거
@EnableJpaAuditing
public class CodeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeBackendApplication.class, args);
	}


}
