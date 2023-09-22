package backend.codebackend.service;

import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Restuarant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    public List<Restuarant> RsData(String address, String categorys) {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("-headless");
//
//

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

        //카테고리를 클릭하지 않았을 경우
        if(!categorys.isEmpty()) {
            WebElement clickCategory = driver.findElement(By.xpath("//span[contains(@class, 'category-name') and text()='" + categorys + "']"));
            System.out.println(clickCategory.getText());
            clickCategory.click();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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

        //선택한 식당 이름을 기준으로 찾을거니 식당 이름을 기준으로 요소를 찾음
        for (WebElement restaurant : restaurants) {
            WebElement parentElement = restaurant.findElement(By.xpath("..")); //restaurant의 바로 부모 요소 선택
            WebElement logoElement = restaurant.findElement(By.xpath("../../..")); //restaurant-name 요소의 부모 부모
            //background-image의 url이 두 개 나오므로 url을 컴마가 붙은 라인부터 지운다(뒤에 url을 지우게 됨)
            String restaurantAddress = logoElement.findElement(By.className("logo")).getCssValue("background-image").replace("url(\"", "").replace("\")","");
            int commaIndex = restaurantAddress.indexOf(",");

            rs = Restuarant.builder()
                    .title(restaurant.getAttribute("title"))
                    .minPrice(parentElement.findElement(By.className("min-price")).getText())
                    .imageUrl(restaurantAddress.substring(0, commaIndex))
                    .icoStar(parentElement.findElement(By.className("ico-star1")).getText())
                    .review_num(parentElement.findElement(By.className("review_num")).getText())
                    .deliveryTime(parentElement.findElement(By.className("delivery-time")).getText())
                    .build();

            rsList.add(rs);
        }
        driver.quit();
        return rsList;
    }




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

        Menu menu;
        List<Menu> menuList = new ArrayList<Menu>();
        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

        // 각 요소의 제목을 확인하여 '두마리치킨'이라는 이름이 포함된 가게를 찾습니다.
        for (WebElement restaurant : restaurants) {
            //restaurant title이 선택한 가게 title 이였을 경우
            if (restaurant.getAttribute("title").equals(restaurantTitle)) {
                restaurant.click();













//                menu = Menu.builder()
//                        .menuName()
//                        .menuDesc()
//                        .menuPrice()
//                        .menuPhoto()
//                        .build();
//
//                menuList.add(menu);
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(menuList);
    }
}
