package backend.codebackend;

import backend.codebackend.domain.*;
import backend.codebackend.dto.ChatDTO;
import backend.codebackend.dto.TotalPrice;
import backend.codebackend.repository.BasketRepository;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class CodeBackendApplicationTests {
	@Autowired MemberService memberService;
	@Autowired MemberRepository MemberRepository;
	@Autowired
	ChatService chatService;
	@Autowired
	ChatUserService chatUserService;
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	BasketService basketService;
	@Autowired
	MozipService mozipService;

	@Test
	@DisplayName("유저 리스트 조회")
	void 유저리스트조회() {
		List<String> result = chatUserService.getUserList(87L);
		List<String> result2 = chatUserService.getUserList(88L);

		System.out.println(result);
		System.out.println(result2);
	}
	@Test
	@DisplayName("모집글 중복 생성 조회")
	void 모집글중복조회() {
		if(mozipService.findNickName("불꽃주먹").isPresent())
			System.out.println("방이 중복됩니다");
		System.out.println("방을 생성할 수 있습니다.");
	}

	@Test
	@DisplayName("모집글 중복 참여 조회")
	void 모집글중복참여조회() {
		if (chatUserService.isDuplicateRoom(111L, "12")){
			System.out.println("방에 참여할 수 있습니다");
		}
		else {
			System.out.println("입장한 방이 있습니다.");
		}
	}


	@Test
	@DisplayName("채팅방의 유저 닉네임 중복 조회")
	void 유저중복조회() {
		if(chatUserService.addUser(87L, "야왕이네"))
			System.out.println("저장 잘됨!");
		else
			System.out.println("저장 안됨!");
	}

	@Test
	@DisplayName("채팅 친 시간 조회")
	void 채팅시간조회() {
		ChatDTO chatDTO = ChatDTO.builder()
						.id(13L)
				        .type(ChatDTO.MessageTYpe.valueOf("ENTER"))
						.sender("야왕")
						.message("테스트테스트테스트테스트테스트테스트테스트테스트테스트")
				        .build();

		Chat chat = chatService.save(chatDTO);
		chatService.timestamp(chat);
	}

	@Test
	@DisplayName("방장 조회 테스트")
	void 방장조회() {

		if (chatUserService.isCurrentUserHost(64L, "김윤준")){
			System.out.println("방장 맞음 ");
		}
	}

	@Test
	@DisplayName("가게 정보 조회 테스트")
	void 가게정보조회() throws InterruptedException {
		Member member = memberService.findLoginId("1234").get();
		System.out.println(member + "님의 주소는 : " + member.getAddress() + "입니다.");
		String category = "";

		RestaurantService restaurantService = new RestaurantService();
		restaurantService.driver();
		restaurantService.loadPage();
		restaurantService.searchAddress("경기 성남시 분당구 대왕판교로385번길 2");

//		if(category != "")
//			restaurantService.selectCategory(category);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}

		List<Restuarant> rs =restaurantService.RsData();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		for(int i = 0; i < rs.size(); i++) {
			System.out.println("가게 이름 : " + rs.get(i).getTitle());
			System.out.println("최소 주문 금액 : " + rs.get(i).getMinPrice());
			System.out.println("이미지 url : " + rs.get(i).getImageUrl());
			System.out.println("별점 : " + rs.get(i).getIcoStar());
			System.out.println("리뷰 개수 : " + rs.get(i).getReview_num());
			System.out.println("배달예정시각 : " + rs.get(i).getDeliveryTime());

		}
	}


	@SneakyThrows
	@Test
	@DisplayName("메뉴 리스트 조회 테스트")
	void 메뉴리스트조회() {
		try {
			Member member = memberService.findLoginId("1234").get();
			System.out.println(member + "님의 주소는 : " + member.getAddress() + "입니다.");
			Future<Menu> m = restaurantService.menuList("24시장안성", member.getAddress());
			Menu menu = m.get();

			for (int i = 0; i < menu.getMenuList_Title().size(); i++) {
				System.out.println("\n\n[" + menu.getMenuList_Title_Name().get(i) + "]");
				for (int j = 0; j < menu.getMenuList_Title().get(i).size(); j++) {
					System.out.println("메뉴 이름 : " + menu.getMenuList_Title().get(i).get(j).getMenuName());
					System.out.println("메뉴 정보 : " + menu.getMenuList_Title().get(i).get(j).getMenuDesc());
					System.out.println("메뉴 가격 : " + menu.getMenuList_Title().get(i).get(j).getMenuPrice());
					System.out.println("메뉴 사진 :  " + menu.getMenuList_Title().get(i).get(j).getMenuPhoto());
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			// ExecutionException 처리 코드
			e.printStackTrace(); // 예외 스택 트레이스 출력
		}
	}

	@SneakyThrows
	@Test
	@DisplayName("최소 주문 금액 및 배달 금액 크롤링 테스트")
	void 배달정보조회() {
		try{
			Member member = memberService.findLoginId("1234").get();
			System.out.println(member + "님의 주소는 : " + member.getAddress() + "입니다.");
			Future<Menu> m = restaurantService.menuList("24시장안성", member.getAddress());
			Menu menu = m.get();

			System.out.println("최소 주문 금액 : " + menu.getMinPrice());
			System.out.println("배달 요금 : " + menu.getDelivery_fee());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			// ExecutionException 처리 코드
			e.printStackTrace(); // 예외 스택 트레이스 출력
		}
	}

	@Test
	@DisplayName("장바구니 담은 메뉴 조회")
	void 장바구니담은메뉴조회() {
		List<Basket> findAll = basketService.findAll(103l);
		System.out.println("\n\n");
		for(int i = 0; i < findAll.size(); i++) {
			System.out.println("채팅방번호 " + findAll.get(i).getChatroom_id() + "메뉴이름 " + findAll.get(i).getProduct_name()
					+ "메뉴가격 " + findAll.get(i).getProduct_name()
					+ "메뉴수량 " + findAll.get(i).getQuantity());
		}
	}
	
	@Test
	@DisplayName("장바구니 메뉴 추가 테스트")
	void 메뉴추가() {
		basketService.addItemToBasket(32l, "허니콤보", 2000, "김윤준");

		List<Basket> findAll = basketService.findAll(32l);
		System.out.println(findAll.get(0).getProduct_name());
	}

	@Autowired
	BasketRepository basketRepository;
	@Test
	@DisplayName("장바구니 메뉴 중복 테스트")
	void 메뉴중복() {
		if(basketRepository.duplicateBasketItem(103l, "포테이토 L", 27900, "야왕이네").isPresent()){
			System.out.println("메뉴 중복");
			Basket basket = basketRepository.duplicateBasketItem(103l, "포테이토 L", 27900, "야왕이네").get();
			System.out.println(basket.getQuantity());
		} else {
			System.out.println("메뉴 없음");
		}

	}

	@Test
	@DisplayName("장바구니 총 금액 조회")
	void 총금액() {
		List<TotalPrice> totalPrices = basketRepository.getTotalPrice(111L);
		for(int i = 0; i < totalPrices.size(); i++) {
			System.out.println(totalPrices.get(i).getUsername());
			System.out.println(totalPrices.get(i).getTotalPrice());
		}
	}

	@Test
	@DisplayName("방 정산 상태 조회")
	void 정산상태() {
		System.out.println(mozipService.mozipStatus(95L));
	}

	@Test
	@DisplayName("방 정산 상태 변경")
	void 정산상태변경() {
		mozipService.calculateStartStatus(95L);
	}

	@Test
	@DisplayName("최근 업데이트 된 장바구니")
	void 최근업뎃장바구니() {
		Basket basket = basketRepository.addItemToBasketReceive("1234");
		System.out.println(basket.getId());
	}
	@Test
	@DisplayName("접속한 방 번호 조회")
	void 접속한방번호조회() {
		Long roomId= chatUserService.findMemberRoomId("1234");
		System.out.println(roomId);
	}

	@Test
	@DisplayName("모집글 삭제 테스트")
	void 모집글_삭제_테스트() {
		Long id = 112L;
		mozipService.deleteChatUsers(id);
		mozipService.deleteMozip(id);


		List<Mozip> mozipList = mozipService.getMozipList();
		for (int i = 0; i < mozipList.size(); i++) {
			System.out.println(mozipList.get(i).getId());
		}

		List<String> chatUserList = chatUserService.getUserList(id);
		if (!chatUserList.isEmpty()) {
			for (int i = 0; i < chatUserList.size(); i++) {
				// 리스트의 각 요소를 출력
				System.out.println(chatUserList.get(i));
			}
		} else {
			// chatUserList가 널일 경우 메시지 출력
			System.out.println("chatUserList가 널입니다.");
		}
	}

	@Test
	@DisplayName("회원 탈퇴")
	void 회원탈퇴() {
		Member m = new Member();
		m.setLogin("testUser");
		m.setPw("testPassword");
		m.setPwcheck("testPassword");
		m.setUsername("Test");
		m.setNickname("Tester");
		m.setAddress("Test Address");
		m.setPnum("1234567890");
		m.setCertified("testCertified");

		Member savedMember = memberRepository.save(m);

		List<Member> result = memberRepository.findAll();
		for (Member member : result) {
			System.out.println(member.getLogin());
			System.out.println(member.getUsername());
		}

		memberService.withdrawMember(savedMember.getLogin());

		Optional<Member> deletedMember = memberRepository.findById(String.valueOf(savedMember.getId()));
		assertThat(deletedMember).isEmpty();

		List<Member> result2 = memberRepository.findAll();
		for (Member member : result) {
			System.out.println(member.getLogin());
			System.out.println(member.getUsername());
		}
	}
	@Test
	@DisplayName("회원 탈퇴2")
	void 회원탈퇴2() {
		String nickname_Delete = "그램";

		Optional<Member> memberToDelete = memberRepository.findByName(nickname_Delete);
		assertThat(memberToDelete).isPresent(); // 삭제 전에 해당 회원이 존재하는지 확인

		memberService.withdrawMember(nickname_Delete);
//		List<Member> result = memberRepository.findAll();
//		for(int i = 0; i < result.size(); i++) {
//			System.out.println(result.get(i).getId());
//			System.out.println(result.get(i).getNickname());
//		}
		Optional<Member> deletedMember = memberRepository.findByName(nickname_Delete);
		assertThat(deletedMember).isEmpty(); // 삭제 후에 해당 회원이 존재하지 않는지 확인
	}
}
