package backend.codebackend.controller;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MenuCrawlerController {

    @GetMapping("/crawl-yogiyo-menu")
    public String crawlYogiyoMenu() throws IOException {
        String url = "https://www.yogiyo.co.kr/your_location"; // 실제 주소로 변경

        // Jsoup을 사용하여 웹페이지 가져오기
        Document document = Jsoup.connect(url).get();

        // 원하는 메뉴 정보를 선택자로 추출
        Elements menuItems = document.select("div.menu-item"); // 적절한 CSS 선택자를 사용

        StringBuilder result = new StringBuilder();

        // 메뉴 정보 출력 또는 저장
        for (Element menuItem : menuItems) {
            String menuName = menuItem.select("h4.name").text();
            String menuPrice = menuItem.select("span.price").text();
            result.append("메뉴: ").append(menuName).append(", 가격: ").append(menuPrice).append("\n");
        }

        return result.toString();
    }

}
