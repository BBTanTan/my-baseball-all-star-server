package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerScoreCrawlerService {

    private final PlayerRepository playerRepository;

    @Scheduled(cron = "${scheduler.crawl}")
    @Transactional
    public void crawl() {
        crawlPitcher();
        crawlCatcher();
    }

    private void crawlPitcher() {
        WebDriver driver = createChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 투수목록
        List<Player> pitchers = playerRepository.findAll().stream()
                .filter(player -> player.getPosition().getName().contains("투수"))
                .toList();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (Player pitcher : pitchers) {
                // 포지션 드롭다운에서 '투수' 선택
                Select positionDropdown = new Select(wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("cphContents_cphContents_cphContents_ddlPosition"))));
                positionDropdown.selectByVisibleText("투수");

                // 검색창 대기 및 입력
                WebElement searchBox = wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
                searchBox.clear();
                searchBox.sendKeys(pitcher.getName());

                // 검색 버튼 클릭 및 검색 결과 대기
                WebElement searchButton = wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("cphContents_cphContents_cphContents_btnSearch")));
                searchButton.click();

                // 선수 링크 대기 및 클릭
                WebElement playerLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a")));
                playerLink.click();

                // 상세 페이지 로딩 대기
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records")));

                // 능력치 가져오기 - 첫 번째 표
                WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr")));
                String era = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // ERA
                String g = row.findElement(By.cssSelector("td:nth-child(3)")).getText();    // G

                // 두번째 표
                row = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr")));
                String so = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // SO
                String whip = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // WHIP
                String avg = row.findElement(By.cssSelector("td:nth-child(12)")).getText(); // AVG
                String qs = row.findElement(By.cssSelector("td:nth-child(13)")).getText(); // QS

                Double score = calculateOverallPitcherScore(Double.valueOf(era), Double.valueOf(whip), Integer.valueOf(so),
                        Integer.valueOf(g), Double.valueOf(avg), Integer.valueOf(qs));
                pitcher.updateScore(score);

                // 첫 페이지로 돌아가기 및 대기
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
            }

        } finally {
            driver.quit();
        }
    }

    // 야수 점수 크롤링 작업
    private void crawlCatcher() {
        WebDriver driver = createChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<Player> players = playerRepository.findAll().stream()
                .filter(player -> !player.getPosition().getName().contains("투수"))
                .toList();

        try {

            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (Player player : players) {
                Select positionDropdown = new Select(wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("cphContents_cphContents_cphContents_ddlPosition"))));

                if (player.getPosition() == Position.CATCHER) {
                    positionDropdown.selectByVisibleText("포수");

                    WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                            By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
                    searchBox.clear();
                    searchBox.sendKeys(player.getName());

                    WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.id("cphContents_cphContents_cphContents_btnSearch")));
                    searchButton.click();
                } else {
                    // 포지션 선택 (외야수 or 내야수)
                    String pos = player.getPosition() == Position.OUT_FIELD ? "외야수" : "내야수";
                    positionDropdown.selectByVisibleText(pos);

                    WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                            By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
                    searchBox.clear();
                    searchBox.sendKeys(player.getName());

                    WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.id("cphContents_cphContents_cphContents_btnSearch")));
                    searchButton.click();

                    // 검색 결과 tbody 대기 및 확인
                    WebElement tbody = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody")));
                    List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                    if (rows.isEmpty()) {
                        if (pos.equals("외야수")) {
                            positionDropdown = new Select(wait.until(ExpectedConditions.elementToBeClickable(
                                    By.id("cphContents_cphContents_cphContents_ddlPosition"))));
                            positionDropdown.selectByVisibleText("내야수");
                        } else if (pos.equals("내야수")) {
                            positionDropdown = new Select(wait.until(ExpectedConditions.elementToBeClickable(
                                    By.id("cphContents_cphContents_cphContents_ddlPosition"))));
                            positionDropdown.selectByVisibleText("외야수");
                        }

                        searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                                By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
                        searchBox.clear();
                        searchBox.sendKeys(player.getName());

                        searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.id("cphContents_cphContents_cphContents_btnSearch")));
                        searchButton.click();
                    }
                }

                // 선수 링크 클릭 대기 및 클릭
                WebElement playerLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a")));
                playerLink.click();

                // 상세 페이지 로딩 대기
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records")));

                // 능력치 가져오기 - 첫 번째 표
                WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr")));
                String avg = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // AVG
                String hr = row.findElement(By.cssSelector("td:nth-child(10)")).getText();    // HR
                String rbi = row.findElement(By.cssSelector("td:nth-child(12)")).getText();    // RBI

                // 두번째 표
                row = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr")));
                String ops = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // OPS
                String so = row.findElement(By.cssSelector("td:nth-child(4)")).getText(); // SO
                String gdp = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // GDP

                Double score = calculateOffensiveScore(Double.valueOf(avg), Integer.valueOf(hr), Integer.valueOf(rbi),
                        Double.valueOf(ops), Integer.valueOf(so), Integer.valueOf(gdp));
                player.updateScore(score);

                // 첫 페이지로 돌아가기 및 대기
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("cphContents_cphContents_cphContents_txtSearchPlayerName")));
            }
        } finally {
            driver.quit();
        }
    }

    //투수 점수 계산
    private double calculateOverallPitcherScore(double era, double whip, int so, int g, double avg, int qs) {
        // 1. 이닝당 삼진 비율 (K/9 기준)
        double kPer9 = (so / (double) g) * 9;
        double pwr = Math.max(0, Math.min((kPer9 - 3.0) / 10.0 * 100, 100));

        // 2. ERA: 0.00 ~ 5.00 기준
        double ctl = Math.max(0, Math.min((5.0 - era) / 5.0 * 100, 100));

        // 3. QS 비율
        double clt = Math.max(0, Math.min(qs / (double) g * 100, 100));

        // 4. 피안타율: 0.300 ~ 0.200 기준
        double hitAvoid = Math.max(0, Math.min((0.300 - avg) / 0.100 * 100, 100));

        // 5. 삼진 수 (멘탈): 0 ~ 200 기준
        double mental = Math.max(0, Math.min(so / 200.0 * 100, 100));

        // 6. WHIP: 1.5 ~ 0.8 기준
        double var = Math.max(0, Math.min((1.5 - whip) / 0.7 * 100, 100));

        // 가중치 합산 (총합 = 1.0)
        double total = pwr * 0.25
                + ctl * 0.25
                + clt * 0.20
                + hitAvoid * 0.15
                + mental * 0.10
                + var * 0.05;

        return Math.round(total * 100.0) / 100.0;
    }

    //야수 & 포수 능력치 계산
    public double calculateOffensiveScore(double avg, int hr, int rbi, double ops, int so, int gdp) {
        // 정규화된 값 (0~100)
        double contact = Math.max(0, Math.min((avg - 0.200) / 0.150 * 100, 100));         // 타율 0.200~0.350
        double power = Math.min(hr / 60.0 * 100, 100);                                     // 최대 60홈런 기준
        double clutch = Math.min(rbi / 120.0 * 100, 100);                                  // 최대 120타점 기준
        double productivity = Math.max(0, Math.min((ops - 0.500) / 0.700 * 100, 100));     // OPS 0.5~1.2
        double discipline = Math.max(0, (1 - so / 200.0) * 100);                           // 0~200삼진 기준
        double smartPlay = Math.max(0, (1 - gdp / 30.0) * 100);                            // 0~30병살 기준

        // 가중치 합산
        double total = contact * 0.20
                + power * 0.20
                + clutch * 0.15
                + productivity * 0.25
                + discipline * 0.10
                + smartPlay * 0.10;

        return Math.round(total * 100.0) / 100.0;
    }
    
    public WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        String userDataDir = "/tmp/chrome-profile-" + UUID.randomUUID();
        new File(userDataDir).mkdirs();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--user-data-dir=" + userDataDir);

        return new ChromeDriver(options);
    }
}
