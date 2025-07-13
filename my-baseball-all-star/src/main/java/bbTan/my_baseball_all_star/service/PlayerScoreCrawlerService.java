package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerScoreCrawlerService {

    private final PlayerRepository playerRepository;

    @Scheduled(cron = "${scheduler.crawl}")
    public void crawl() {
        crawlPitcher();
        crawlCatcher();
    }

    private void crawlPitcher() {
        WebDriver driver = createChromeDriver();

        //투수목록
        List<Player> pitchers = playerRepository.findAll().stream()
                .filter(player -> player.getPosition().getName().contains("투수"))
                .toList();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            //투수
            for (Player pitcher : pitchers) {
                // 1. 선수 이름 검색창에 입력

                // 포지션 드롭다운에서 '투수' 선택
                Select positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                positionDropdown.selectByVisibleText("투수");
                Thread.sleep(2000); // 검색창에서 처리 시간 대기

                WebElement searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                searchBox.clear();
                searchBox.sendKeys(pitcher.getName());
                Thread.sleep(1000); // 검색창에서 처리 시간 대기

                // 2. 검색 버튼 클릭
                WebElement searchButton = driver.findElement(By.id("cphContents_cphContents_cphContents_btnSearch"));
                searchButton.click();
                Thread.sleep(2000); // 검색 결과가 나오도록 대기

                // 3. 해당 선수의 링크 클릭하여 상세 페이지로 이동
                WebElement playerLink = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a"));
                playerLink.click();
                Thread.sleep(2000); // 상세 페이지가 로드될 시간을 대기
                // 4. 상세 페이지에서 원하는 정보 가져오기

                //능력치 가져오기
                WebElement row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr"));

                //세부 능력치 가져오기
                String era = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // ERA
                String g = row.findElement(By.cssSelector("td:nth-child(3)")).getText();    // G

                //두번째 표
                row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"));
                String so = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // SO
                String whip = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // WHIP
                String avg = row.findElement(By.cssSelector("td:nth-child(12)")).getText(); // AVG
                String qs = row.findElement(By.cssSelector("td:nth-child(13)")).getText(); // QS

                Double score = calculateOverallPitcherScore(Double.valueOf(era),Double.valueOf(whip),Integer.valueOf(so),Integer.valueOf(g),Double.valueOf(avg),Integer.valueOf(qs));
                pitcher.updateScore(score);

                // 5. 상세 페이지에서 데이터 추출 후 첫 페이지로 돌아가기
                driver.navigate().back();
                Thread.sleep(2000); // 페이지가 돌아오는 시간 대기
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit(); // 리소스 정리
        }
    }

    //야수 점수 크롤링 작업
    private void crawlCatcher() {
        WebDriver driver = createChromeDriver();
        //투수 제외 모두 선택
        List<Player> players = playerRepository.findAll().stream()
                .filter(player -> !player.getPosition().getName().contains("투수"))
                .toList();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            //포수
            for (Player player : players) {
                // 1. 선수 이름 검색창에 입력

                Select positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                if (player.getPosition() == Position.CATCHER) {
                    positionDropdown.selectByVisibleText("포수");
                    Thread.sleep(2000); // 검색창에서 처리 시간 대기

                    WebElement searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                    searchBox.clear();
                    searchBox.sendKeys(player.getName());
                    Thread.sleep(1000); // 검색창에서 처리 시간 대기

                    // 2. 검색 버튼 클릭
                    WebElement searchButton = driver.findElement(By.id("cphContents_cphContents_cphContents_btnSearch"));
                    searchButton.click();
                    Thread.sleep(2000); // 검색 결과가 나오도록 대기
                }

                //야수
                else {
                    // 포지션 드롭다운에서 포지션 선택
                    String pos = player.getPosition() == Position.OUT_FIELD ? "외야수" : "내야수";
                    positionDropdown.selectByVisibleText(pos);
                    Thread.sleep(2000); // 검색창에서 처리 시간 대기

                    WebElement searchBox = driver.findElement(
                            By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                    searchBox.clear();
                    searchBox.sendKeys(player.getName());
                    Thread.sleep(1000); // 검색창에서 처리 시간 대기

                    // 2. 검색 버튼 클릭
                    WebElement searchButton = driver.findElement(
                            By.id("cphContents_cphContents_cphContents_btnSearch"));
                    searchButton.click();
                    Thread.sleep(2000); // 검색 결과가 나오도록 대기

                    //포지션 변경 등 이유로 검색되지 않을 경우
                    WebElement tbody = driver.findElement(By.cssSelector(
                            "#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody"));
                    List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                    if (rows.isEmpty()) {
                        if (pos.equals("외야수")) {
                            // 내야수로 드롭다운 변경
                            positionDropdown = new Select(
                                    driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                            positionDropdown.selectByVisibleText("내야수");
                            Thread.sleep(1000);
                        }
                        if (pos.equals("내야수")) {
                            // 외야수로 드롭다운 변경
                            positionDropdown = new Select(
                                    driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                            positionDropdown.selectByVisibleText("외야수");
                            Thread.sleep(1000);
                        }

                        // 검색 다시 수행
                        searchBox = driver.findElement(
                                By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                        searchBox.clear();
                        searchBox.sendKeys(player.getName());
                        Thread.sleep(1000);

                        searchButton = driver.findElement(By.id("cphContents_cphContents_cphContents_btnSearch"));
                        searchButton.click();
                        Thread.sleep(2000);
                    }
                }

                // 3. 해당 선수의 링크 클릭하여 상세 페이지로 이동
                WebElement playerLink = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a"));
                playerLink.click();
                Thread.sleep(2000); // 상세 페이지가 로드될 시간을 대기
                // 4. 상세 페이지에서 원하는 정보 가져오기

                //능력치 가져오기
                WebElement row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.tbl-type02-pd0.mb35 > table > tbody > tr"));

                //세부 능력치 가져오기
                String avg = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // AVG
                String hr = row.findElement(By.cssSelector("td:nth-child(10)")).getText();    // hr
                String rbi = row.findElement(By.cssSelector("td:nth-child(12)")).getText();    // rbi

                //두번째 표
                row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"));

                String ops = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // ops
                String so = row.findElement(By.cssSelector("td:nth-child(4)")).getText(); // so
                String gdp = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // gdp

                Double score = calculateOffensiveScore(Double.valueOf(avg),Integer.valueOf(hr),Integer.valueOf(rbi),
                        Double.valueOf(ops),Integer.valueOf(so),Integer.valueOf(gdp));

                player.updateScore(score);

                // 5. 상세 페이지에서 데이터 추출 후 첫 페이지로 돌아가기
                driver.navigate().back();
                Thread.sleep(2000); // 페이지가 돌아오는 시간 대기
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit(); // 리소스 정리
        }

    }

    //투수 점수 계산
    private double calculateOverallPitcherScore(double era, double whip, int so, int g, double avg, int qs) {
        double pwr = Math.min(so / (double) g * 10, 100); // 구위
        double ctl = Math.max(100 - era * 20, 0);         // 제구
        double clt = Math.min(qs / (double) g * 100, 100); // 위기관리
        double hitAvoid = Math.max(100 - avg * 100, 0);   // 피안타 회피
        double mental = Math.max(100 - (32.0 / so) * 100, 0); // 멘탈 (실점 대비 삼진)
        double var = Math.max(100 - whip * 20, 0);        // 변화구 제구력 (WHIP 기반)

        return Math.round((
                pwr * 0.25 +
                        ctl * 0.25 +
                        clt * 0.20 +
                        hitAvoid * 0.15 +
                        mental * 0.10 +
                        var * 0.05
        ) * 100.0) / 100.0;
    }

    //야수 & 포수 능력치 계산
    public double calculateOffensiveScore(double avg, int hr, int rbi, double ops, int so, int gdp) {
        double contact = avg * 100;
        double power = hr * 2;
        double clutch = rbi * 0.5;
        double productivity = ops * 50;
        double discipline = Math.max(100 - so * 0.2, 0);
        double smartPlay = Math.max(100 - gdp * 0.5, 0);

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
