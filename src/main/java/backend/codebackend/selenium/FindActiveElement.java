package backend.codebackend.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class FindActiveElement {
    public static void main(String[] args) {
        // WebDriver 객체를 생성합니다.
        WebDriver driver = new ChromeDriver();

        // Google 웹 페이지를 엽니다.
        driver.get("https://www.yogiyo.co.kr/mobile/#/");


        // 검색창을 찾습니다. Google의 검색창은 'name' 속성이 'q'인 input 요소입니다.
        WebElement searchBox = driver.findElement(By.name("address_input"));

        searchBox.clear();

        searchBox.click();

        // 검색창에 "apple"을 입력합니다.
        searchBox.sendKeys("수도 오피스텔");

        WebElement clickSearch = driver.findElement(By.className("ico-pick"));
        clickSearch.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 가게 이름과 최소주문금액을 저장할 리스트 생성
        List<String> restaurantNames = new ArrayList<>();
        List<String> minPrices = new ArrayList<>();
        // 웹 페이지에서 restaurant-name 클래스를 가진 모든 요소를 선택하여 restaurants 리스트에 저장
        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

        // 각 요소의 제목을 확인하여 '두마리치킨'이라는 이름이 포함된 가게를 찾습니다.
        //restaurants 리스트에 있는 모든 요소를 순회하면서 '치킨'이 포함된 가게를 찾음
        for (WebElement restaurant : restaurants) {
            if (restaurant.getAttribute("title").contains("치킨")) {
                // 원하는 가게를 찾았습니다. 이 가게의 조부모 요소 중 'item' 클래스를 가진 요소를 찾습니다.
//                restaurant.click();
                //XPath를 사용하여 현재 웹 요소의 부모 요소를 찾는 표현식으로
                // ..은 XPath에서 현재 요소의 부모를 나타내는 특별한 기호.
                WebElement parentElement = restaurant.findElement(By.xpath(".."));
                String minPrice = parentElement.findElement(By.className("min-price")).getText();

                // 가게 이름을 restaurantNames 리스트에 추가
                restaurantNames.add(restaurant.getAttribute("title"));

                // 최소 주문금액을 minPrices 리스트에 추가
                minPrices.add(minPrice);
                // 검색 결과 출력 또는 저장
                for (int i = 0; i < restaurantNames.size(); i++) {
                    System.out.println("가게 이름: " + restaurantNames.get(i));
                    System.out.println("최소 주문금액: " + minPrices.get(i));
                }
            }
        }
        // 검색을 수행합니다. 이를 위해 엔터 키를 입력합니다.
        searchBox.sendKeys(Keys.RETURN);

        // WebDriver를 종료합니다.
        //driver.quit();
    }
}
