package backend.codebackend.controller;

import backend.codebackend.domain.Restuarant;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantsController {

    private final RestaurantService restaurantService;
    private final MemberService memberService;

    @GetMapping()
    public List<Restuarant> restaurantsList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("세션이 없습니다.");
        }

        return restaurantService.RsData(memberService.findLoginId(String.valueOf(session.getAttribute("memberId"))).getAdderss());
    }
}
