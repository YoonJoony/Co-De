package backend.codebackend.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class YogiyoMenuCrawler {
    public static void main(String[] args) {



        // WebDriver 인스턴스 생성
        WebDriver driver = new ChromeDriver();

        // 구글 웹 페이지 열기
        driver.get("https://www.google.com");

        // 검색어 입력
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("의자");
        searchBox.sendKeys(Keys.RETURN);

        // 검색 결과 페이지 기다리기 (여기서는 간단하게 5초로 설정)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 검색 결과 출력 (여기서는 타이틀만 출력)
        java.util.List<WebElement> searchResults = driver.findElements(By.cssSelector(".tF2Cxc"));
        for (WebElement result : searchResults) {
            System.out.println(result.getText());
        }

        // WebDriver 종료
        driver.quit();
    }
 }
