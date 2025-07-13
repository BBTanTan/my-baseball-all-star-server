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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Player> pitchers = playerRepository.findAll().stream()
                .filter(player -> player.getPosition().getName().contains("투수"))
                .toList();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (Player pitcher : pitchers) {
                Select positionDropdown = new Select(waitForElement(By.id("cphContents_cphContents_cphContents_ddlPosition"), driver, 5));
                positionDropdown.selectByVisibleText("투수");

                WebElement searchBox = waitForElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"), driver, 5);
                searchBox.clear();
                searchBox.sendKeys(pitcher.getName());

                WebElement searchButton = waitForElement(By.id("cphContents_cphContents_cphContents_btnSearch"), driver, 5);
                searchButton.click();

                WebElement playerLink = waitForElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a"), driver, 5);
                playerLink.click();

                WebElement row = waitForElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr"), driver, 5);
                String era = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
                String g = row.findElement(By.cssSelector("td:nth-child(3)")).getText();

                row = waitForElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"), driver, 5);
                String so = row.findElement(By.cssSelector("td:nth-child(5)")).getText();
                String whip = row.findElement(By.cssSelector("td:nth-child(11)")).getText();
                String avg = row.findElement(By.cssSelector("td:nth-child(12)")).getText();
                String qs = row.findElement(By.cssSelector("td:nth-child(13)")).getText();

                Double score = calculateOverallPitcherScore(Double.valueOf(era), Double.valueOf(whip), Integer.valueOf(so), Integer.valueOf(g), Double.valueOf(avg), Integer.valueOf(qs));
                pitcher.updateScore(score);

                driver.navigate().back();
                waitForElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"), driver, 5);
            }

        } finally {
            driver.quit();
        }
    }

    private void crawlCatcher() {
        WebDriver driver = createChromeDriver();
        List<Player> players = playerRepository.findAll().stream()
                .filter(player -> !player.getPosition().getName().contains("투수"))
                .toList();

        try {

            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (Player player : players) {
                Select positionDropdown = new Select(waitForElement(By.id("cphContents_cphContents_cphContents_ddlPosition"), driver, 5));

                String positionText = switch (player.getPosition()) {
                    case CATCHER -> "포수";
                    case OUT_FIELD -> "외야수";
                    default -> "내야수";
                };
                positionDropdown.selectByVisibleText(positionText);

                WebElement searchBox = waitForElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"), driver, 5);
                searchBox.clear();
                searchBox.sendKeys(player.getName());

                WebElement searchButton = waitForElement(By.id("cphContents_cphContents_cphContents_btnSearch"), driver, 5);
                searchButton.click();

                try {
                    WebElement tbody = waitForElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody"), driver, 3);
                    List<WebElement> rows = tbody.findElements(By.tagName("tr"));
                    if (rows.isEmpty()) {
                        String altPos = positionText.equals("외야수") ? "내야수" : "외야수";
                        positionDropdown = new Select(waitForElement(By.id("cphContents_cphContents_cphContents_ddlPosition"), driver, 5));
                        positionDropdown.selectByVisibleText(altPos);
                        searchBox = waitForElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"), driver, 5);
                        searchBox.clear();
                        searchBox.sendKeys(player.getName());
                        searchButton = waitForElement(By.id("cphContents_cphContents_cphContents_btnSearch"), driver, 5);
                        searchButton.click();
                    }
                } catch (TimeoutException ignored) {
                    continue;
                }

                WebElement playerLink = waitForElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a"), driver, 5);
                playerLink.click();

                WebElement row = waitForElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr"), driver, 5);
                String avg = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
                String hr = row.findElement(By.cssSelector("td:nth-child(10)")).getText();
                String rbi = row.findElement(By.cssSelector("td:nth-child(12)")).getText();

                row = waitForElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"), driver, 5);
                String ops = row.findElement(By.cssSelector("td:nth-child(11)")).getText();
                String so = row.findElement(By.cssSelector("td:nth-child(4)")).getText();
                String gdp = row.findElement(By.cssSelector("td:nth-child(5)")).getText();

                Double score = calculateOffensiveScore(Double.valueOf(avg), Integer.valueOf(hr), Integer.valueOf(rbi),
                        Double.valueOf(ops), Integer.valueOf(so), Integer.valueOf(gdp));

                player.updateScore(score);
                driver.navigate().back();
                waitForElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"), driver, 5);
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

    private WebElement waitForElement(By by, WebDriver driver, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

}
