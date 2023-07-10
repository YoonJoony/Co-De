package backend.codebackend;

import backend.codebackend.controller.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class CodeBackendApplicationTests {
	@Autowired MemberService memberService;
	@Autowired MemberRepository MemberRepository;



	@Test
	@DisplayName("회원가입 성공")
	void 회원가입() {
		MemberForm memberForm = new MemberForm("123", "123", "123", "123", "123", "123");
		String id = memberService.join(memberForm);

		Member findMember = memberService.findOne(id).get();
		assertThat(memberForm.getNickname()).isEqualTo(findMember.getNickname());
		System.out.println("MemberForm : \n" + memberForm.getNickname());
		System.out.println("findMember : \n" + findMember.getNickname());
	}

	@Test
	@DisplayName("중복회원 검사")
	void 중복회원_검사() {
		MemberForm memberForm1 = new MemberForm("123", "123", "123", "김윤준", "123", "123");
		MemberForm memberForm2 = new MemberForm("234", "234", "234", "김윤준", "234", "123");

		memberService.join(memberForm1);
		IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(memberForm2));
		assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다."); //예외 클래스에서 넘어온 메시지랑 해당 메시지랑 같은지 비교
	}

}
