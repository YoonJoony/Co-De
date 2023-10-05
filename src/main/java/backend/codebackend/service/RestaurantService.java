package backend.codebackend.service;

import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Restuarant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.Angle;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class RestaurantService {
    private WebDriver driver;
    private WebDriverWait wait;

    public void driver() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        this.driver = new ChromeDriver();
    }


    public void loadPage() {
        driver.get("https://www.yogiyo.co.kr/mobile/#/");
    }

    public void searchAddress(String address) {
        WebElement searchBox = driver.findElement(By.name("address_input"));
        searchBox.clear();
        searchBox.click();
        searchBox.sendKeys(address);
        WebElement clickSearch = driver.findElement(By.className("ico-pick"));
        clickSearch.click();
    }

    public void selectCategory(String category) {
        WebElement clickCategory = driver.findElement(By.xpath("//span[contains(@class, 'category-name') and text()='" + category + "']"));
        clickCategory.click();
    }


    public List<Restuarant> RsData() {
        // 가게 이름과 최소주문금액을 저장할 리스트 생성
        Restuarant rs;
        List<Restuarant> rsList = new ArrayList<Restuarant>();
        // 웹 페이지에서 restaurant-name 클래스를 가진 모든 요소를 선택하여 restaurants 리스트에 저장
        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

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
        return rsList;
    }

    public void quitDriver() {
        driver.quit();
    }

    //모집글 생성 후 선택한 가게 메뉴를 스레드로 크롤링

    @Async
    public Future<Menu> menuList(String restaurantTitle, String address){
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

        Menu menu; //메뉴 정보 저장시 선언한 Menu 클래스 객체
        Menu menu2; //타이틀 별로 메뉴를 저장하기 위해 선언한 객체
        List<Menu> menuList; //메뉴 저장용 리스트
        List<WebElement> restaurants = driver.findElements(By.className("restaurant-name"));

        menu2 = Menu.builder()
                .menuList_Title(new ArrayList<List<Menu>>())
                .menuList_Title_Name(new ArrayList<String>())
                .build();
        menu2.getMenuList_Title_Name().add("인기메뉴");

        // 각 요소의 제목을 확인하여 사용자가 선택한 가게를 찾아서 클릭함.
        for (WebElement restaurant : restaurants) {
            //restaurant title이 선택한 가게 title 이였을 경우
            if (restaurant.getAttribute("title").equals(restaurantTitle)) {
                restaurant.click();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //메뉴 전체 div
                WebElement popMenu = driver.findElement(By.className("panel-group"));
                //메뉴 타이틀(판넬) 별로 요소 저장함(인기메뉴, 한마리치킨, 세트메뉴)
                List<WebElement> panel = popMenu.findElements(By.className("sub-list"));

                //메뉴 타이틀을 전부 열어야 메뉴를 담을 수 있어서 전부 열음
                for(int i = 2; i < panel.size()-1; i++) {
                    WebElement title = panel.get(i).findElement(By.xpath("../../..")).findElement(By.className("panel-title"));
                    menu2.getMenuList_Title_Name().add(title.findElement(By.className("menu-name")).getText());
                    title.click();
                }

                //저장된 판넬 반복문으로 돌림 (1부터 시작하는 이유 : 판낼 0번째와 마지막은 이상한 요소 잡혀서 제외해줌)
                for (int i = 1; i < panel.size()-1; i++) {
                    menuList = new ArrayList<>(); //menu의 이중 List에 담길 리스트 생성
                    //이중 리스트로 하는 이유 : 메뉴 타이틀(인기메뉴, 한마리치킨...) 별로
                    //메뉴를 저장하기 위함. -> 리스트1(인기메뉴), 리스트2(한마리치킨)...
                    //저장된 판낼 안의 각각의 메뉴의 부모 요소가 되는 photo-menu를 리스트에 저장(메뉴가 여러개니까 리스트로)
                    List<WebElement> item = panel.get(i).findElements(By.className("photo-menu"));
                    for (WebElement m : item) {
                        String menuPhoto = m.findElement(By.className("photo"))
                                .getCssValue("background-image")
                                .replace("url(\"", "")
                                .replace("\")","");
                        int commaIndex = menuPhoto.indexOf(",");
                        menu = Menu.builder()
                                .menuName(m.findElement(By.className("menu-name")).getText())
                                .menuDesc(m.findElement(By.className("menu-desc")).getText())
                                .menuPrice(m.findElement(By.className("menu-price")).findElement(By.className("ng-binding")).getText())
                                .menuPhoto(menuPhoto.substring(0,commaIndex))
                                .build();
                        menuList.add(menu);
                    }
                    menu2.getMenuList_Title().add(menuList);
                }
                break;
            }
        }
        driver.quit();
        return new AsyncResult<>(menu2) ;
    }
}
