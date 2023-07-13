package backend.codebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CodeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeBackendApplication.class, args);
	}


}
