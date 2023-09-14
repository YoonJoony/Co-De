package backend.codebackend.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Keys;

import java.util.ArrayList;
import java.util.List;

public class ExSelenium {
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

        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

        // 각 요소의 제목을 확인하여 '두마리치킨'이라는 이름이 포함된 가게를 찾습니다.
        for (WebElement restaurant : restaurants) {
            if (restaurant.getAttribute("title").contains("치킨")) {
                // 원하는 가게를 찾았습니다. 이 가게의 조부모 요소 중 'item' 클래스를 가진 요소를 찾습니다.
//                restaurant.click();
                WebElement parentElement = restaurant.findElement(By.xpath(".."));
                String minPrice = parentElement.findElement(By.className("min-price")).getText();

                System.out.println("가게 이름: " + restaurant.getAttribute("title"));
                System.out.println("최소 주문금액: " + minPrice);

                //System.out.println(restaurant.getAttribute("min-price"));

            }
        }

        // 검색을 수행합니다. 이를 위해 엔터 키를 입력합니다.
        searchBox.sendKeys(Keys.RETURN);

        // WebDriver를 종료합니다.
        //driver.quit();
    }
}
