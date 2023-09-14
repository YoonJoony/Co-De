package backend.codebackend.service;

import backend.codebackend.domain.Restuarant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    public List<Restuarant> RsData(String address) {

        // WebDriver 객체를 생성합니다.
        WebDriver driver = new ChromeDriver();

        // Google 웹 페이지를 엽니다.
        driver.get("https://www.yogiyo.co.kr/mobile/#/");


        // 검색창을 찾습니다. Google의 검색창은 'name' 속성이 'q'인 input 요소입니다.
        WebElement searchBox = driver.findElement(By.name("address_input"));

        searchBox.clear();

        searchBox.click();

        // 검색창에 "apple"을 입력합니다.
        searchBox.sendKeys(address);

        WebElement clickSearch = driver.findElement(By.className("ico-pick"));
        clickSearch.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 가게 이름과 최소주문금액을 저장할 리스트 생성
        Restuarant rs;
        List<Restuarant> rsList = new ArrayList<Restuarant>();
        // 웹 페이지에서 restaurant-name 클래스를 가진 모든 요소를 선택하여 restaurants 리스트에 저장
        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

        // 스크롤을 내리는 JavaScript 코드를 실행합니다.
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 페이지의 끝까지 스크롤을 내립니다.

        int prevSize = 0;
        int currentSize = restaurants.size();

        while(prevSize < currentSize) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            prevSize = restaurants.size();

            restaurants = driver.findElements(By.className("restaurant-name"));

            currentSize = restaurants.size();
        }

        //restaurants 리스트에 있는 모든 요소를 순회하면서 '치킨'이 포함된 가게를 찾음
        for (WebElement restaurant : restaurants) {
            WebElement parentElement = restaurant.findElement(By.xpath(".."));

            rs = Restuarant.builder()
                    .title(restaurant.getAttribute("title"))
                    .minPrice(parentElement.findElement(By.className("min-price")).getText())
                    .build();

            rsList.add(rs);
        }

        return rsList;
    }
        // 검색을 수행합니다. 이를 위해 엔터 키를 입력합니다.
        // WebDriver를 종료합니다.
        //driver.quit();
}
