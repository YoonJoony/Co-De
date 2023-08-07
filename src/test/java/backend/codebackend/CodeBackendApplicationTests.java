package backend.codebackend;

import backend.codebackend.dto.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.MemberService;
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
	void 닉네임조회() {
		MemberForm memberForm = new MemberForm("1010", "1010", "1010", "1010", "1010", "1010");
		Member member = memberService.signUp(memberForm);
		String id = member.getLoginId();

		Member findMember = memberService.findOne(id).get();
		assertThat(member.getNickname()).isEqualTo(findMember.getNickname());
		System.out.println("MemberForm : \n" + member.getNickname());
		System.out.println("findMember : \n" + findMember.getNickname());
	}



}
