package backend.codebackend.service;

import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Mozip;
import backend.codebackend.domain.Restuarant;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class RestaurantService {
    // TODO 수정사항 : By.xpath("//span[contains.. 처럼 크롤링하는 코드는 셀레니움 백그라운드로 변경 시 안될 가능성 있음.
    // TODO 수정사항(24.03.17) : 왠지 모르곘는데 백그라운드로 이제 잘 실행 됨. (주소를 잘 치자)
    // TODO 수정사항(24.04.13) : 동시성 문제가 무조건 발생하는데 초기에 싱글톤 클래스에서 driver 인스턴스를 한개만 관리되게끔 만들어 놨음.
    //  이제 RestaurantService 객체는 한개만 실행되돼 각 driver,wait 인스턴스는 Map에서 여러개로 관리되게 수정함. (동시성 제어)
    private static RestaurantService restaurantService;
    private final Map<String, WebDriver> drivers = new HashMap<>(); // 접속한 세션으로 드라이버 세션 관리함.
    private final Map<String, WebDriverWait> waitMap = new HashMap<>(); // wait : 주로 페이지의 특정 요소가 나타나길 기다리는 용도.

    // 싱글톤으로 객체 생성
    public static RestaurantService getInstance() {
        if(restaurantService == null) {
            restaurantService = new RestaurantService();
        }
        return restaurantService;
    }

    // WebDriver를 세션을 비교하여 생성/가져오기
    public WebDriver driver(String memberId) {
        if (!drivers.containsKey(memberId)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("window-size=1400,1500");
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe"); //크롬 드라이버.exe 위치 지정

            WebDriver driver = new ChromeDriver(options);
            drivers.put(memberId, driver);
        }
        return drivers.get(memberId);
    }
    // WebDriverWait을 세션을 비교하여 생성/가져오기
    public WebDriverWait getWait(String memberId) {
        if(!waitMap.containsKey(memberId)) {
            WebDriver driver = driver(memberId);
            waitMap.put(memberId, new WebDriverWait(driver, Duration.ofSeconds(10)));
        }
        return waitMap.get(memberId);
    }

    public void loadPage(WebDriver driver) {
        driver.get("https://www.yogiyo.co.kr/mobile/#/");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchAddress(String address, WebDriver driver, WebDriverWait wait) {
        //현재 창 주소 입력받음
        String currentUrl = driver.getCurrentUrl();

        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.name("address_input")));

        searchBox.clear();

        searchBox.click();

        searchBox.sendKeys(address);

        WebElement clickSearch = wait.until(ExpectedConditions.elementToBeClickable(By.className("ico-pick")));
        clickSearch.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

        //다음 창 주소 입력받음
        String expectedUrl = driver.getCurrentUrl();

        //현재 창이 유지 될 경우 -> 주소가 잘못 되었을 경우
        if(Objects.equals(currentUrl, expectedUrl)) {
            // dropdown-menu 요소를 찾습니다.
            WebElement dropdownMenu = driver.findElement(By.className("dropdown-menu"));
            // dropdown-menu 요소의 세번째 자식 요소를 찾습니다.
            WebElement thirdChild = dropdownMenu.findElement(By.xpath("./*[3]"));
            // 세번째 자식 요소를 클릭합니다.
            thirdChild.click();
        }
    }

    public void selectCategory(String category, WebDriverWait wait) {
        WebElement clickCategory = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(@class, 'category-name') and text()='" + category + "']")));
        clickCategory.click();
    }


    //가게 리스트 조회
    public List<Restuarant> RsData(WebDriverWait wait) {
        // 가게 이름과 최소주문금액을 저장할 리스트 생성
        Restuarant rs;
        List<Restuarant> rsList = new ArrayList<>();
        // 웹 페이지에서 restaurant-name 클래스를 가진 모든 요소를 선택하여 restaurants 리스트에 저장
        List<WebElement> restaurants = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("restaurant-name")));

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


    //배달 가격 조회
    public List<Integer> searchDeliveryInfo(String restaurantTitle, WebDriverWait wait) throws InterruptedException {
        List<Integer> deliveryInfos = new ArrayList<>();

        List<WebElement> restaurants = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("restaurant-name")));

        // 각 요소의 제목을 확인하여 사용자가 선택한 가게를 찾아서 클릭함.
        for (WebElement restaurant : restaurants) {
            //restaurant title이 선택한 가게 title 이였을 경우
            if (restaurant.getAttribute("title").equals(restaurantTitle)) {
                restaurant.click();
                Thread.sleep(1000);

                // 최소주문금액 요소 검색 후 값을 가져오기까지 기다립니다.
                String minInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(), '최소주문금액')]/span[@class='ng-binding']"))).getText().replaceAll("[^0-9]", "");
                String delInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), '배달요금')]"))).getText().replaceAll("[^0-9]", "");
                deliveryInfos.add(Integer.parseInt(minInfo));
                deliveryInfos.add(Integer.parseInt(delInfo));
                break;
            }
        }
        return deliveryInfos;
    }



    @Async
    public CompletableFuture<List<Menu>> menuList(Mozip mozip, WebDriver driver, WebDriverWait wait, String memberId) throws InterruptedException {
        // Google 웹 페이지를 엽니다.
        List<Menu> menus = new ArrayList<>(); //메뉴 정보 저장시 선언한 Menu 클래스 객체
        List<String> titles = new ArrayList<>();
        titles.add("🔥 인기메뉴");

        //메뉴 전체 div
        WebElement popMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("panel-group")));
        //메뉴 타이틀(판넬) 별로 요소 저장함(인기메뉴, 한마리치킨, 세트메뉴)
        List<WebElement> panel = popMenu.findElements(By.className("sub-list"));

        //메뉴 타이틀을 전부 열어야 메뉴를 담을 수 있어서 전부 열음
        for(int i = 2; i < panel.size()-1; i++) {
            WebElement title = panel.get(i).findElement(By.xpath("../../..")).findElement(By.className("panel-title"));
            titles.add(title.findElement(By.className("menu-name")).getText());
            title.click();
        }

        //저장된 판넬 반복문으로 돌림 (1부터 시작하는 이유 : 판낼 0번째와 마지막은 이상한 요소 잡혀서 제외해줌)
        for (int i = 1; i < panel.size()-1; i++) {
            //저장된 판낼 안의 각각의 메뉴의 부모 요소가 되는 photo-menu를 리스트에 저장(메뉴가 여러개니까 리스트로)
            List<WebElement> item = panel.get(i).findElements(By.className("photo-menu"));
            Menu menu;
            for (WebElement m : item) {
                String menuPhoto = m.findElement(By.className("photo"))
                        .getCssValue("background-image")
                        .replace("url(\"", "")
                        .replace("\")","");
                int commaIndex = menuPhoto.indexOf(",");
                menu = Menu.builder()
                        .mozipId(mozip)
                        .menuName(m.findElement(By.className("menu-name")).getText())
                        .menuPrice(m.findElement(By.className("menu-price")).findElement(By.className("ng-binding")).getText())
                        .menuDesc(m.findElement(By.className("menu-desc")).getText())
                        .menuPhoto(menuPhoto.substring(0,commaIndex))
                        .menuTitle(titles.get(i-1))
                        .build();
                menus.add(menu);
            }
        }

        driver.quit();
        drivers.remove(memberId);
        return CompletableFuture.completedFuture(menus);
    }

    public void quitDriver(String memberId) {
        if (drivers.containsKey(memberId)) {
            WebDriver driver = drivers.get(memberId);
            driver.quit();
            waitMap.remove(memberId);
            drivers.remove(memberId);
        }
    }
}
