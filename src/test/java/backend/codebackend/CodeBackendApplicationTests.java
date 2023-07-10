package backend.codebackend;

import backend.codebackend.controller.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeBackendApplicationTests {

	@Autowired
	MemberService memberService;
	MemberRepository memberRepository;

//	@Test
//	void 회원가입() {
//		Member member = new MemberForm()
//	}

}
