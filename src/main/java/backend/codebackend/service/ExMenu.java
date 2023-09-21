package backend.codebackend.service;

import backend.codebackend.domain.Menu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ExMenu {
    //모집글 생성 후 선택한 가게 메뉴를 스레드로 크롤링
    @Async
    public CompletableFuture<List<Menu>> menuList(String restaurantTitle, String address){
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver();

        // Google 웹 페이지를 엽니다.
        driver.get("https://www.yogiyo.co.kr/mobile/#/");


        // 검색창을 찾습니다. Google의 검색창은 'name' 속성이 'q'인 input 요소입니다.
        WebElement searchBox = driver.findElement(By.name("address_input"));

        searchBox.clear();

        searchBox.click();

        searchBox.sendKeys(address);

        WebElement clickSearch = driver.findElement(By.className("ico-pick"));
        clickSearch.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //메뉴의 이름, 정보, 가격, 사진을 저장할 메뉴 리스트 생성
        Menu menu;
        List<Menu> menuList = new ArrayList<Menu>();
        List<WebElement> menuItems = driver.findElements(By.className("menu-item"));

        for (WebElement  menuItem : menuItems) {
            //각 메뉴 요소에서 메뉴 정보를 가져옴
            String menuName = menuItem.findElement(By.cssSelector(".menu-name-class")).getText();
            String menuDesc = menuItem.findElement(By.cssSelector(".menu-info-class")).getText();
            String menuPrice = menuItem.findElement(By.cssSelector(".menu-price-class")).getText();
            String menuPhoto = menuItem.findElement(By.cssSelector(".menu-photo-class")).getAttribute("src");

                menu = Menu.builder()
                        .menuName(menuName)
                        .menuDesc(menuDesc)
                        .menuPrice(menuPrice)
                        .menuPhoto(menuPhoto)
                        .build();

                menuList.add(menu);

        }

        driver.quit();
        return CompletableFuture.completedFuture(menuList);
    }
}
