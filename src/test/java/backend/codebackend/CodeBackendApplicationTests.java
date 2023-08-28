package backend.codebackend;

import backend.codebackend.dto.MemberForm;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.ChatUserService;
import backend.codebackend.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class CodeBackendApplicationTests {
	@Autowired MemberService memberService;
	@Autowired MemberRepository MemberRepository;
	@Autowired
	ChatUserService chatUserService;


	@Test
	@DisplayName("유저 리스트 조회")
	void 유저리스트조회() {
		List<String> result = chatUserService.getUserList(87L);
		List<String> result2 = chatUserService.getUserList(88L);

		System.out.println(result);
		System.out.println(result2);
	}

	@Test
	@DisplayName("채팅방의 유저 닉네임 중복 조회")
	void 유저중복조회() {
		if(chatUserService.addUser(87L, "야왕이네"))
			System.out.println("저장 잘됨!");
		else
			System.out.println("저장 안됨!");
	}



}
