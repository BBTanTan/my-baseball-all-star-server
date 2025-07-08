package bbTan.my_baseball_all_star.global.utils;

import bbTan.my_baseball_all_star.domain.Player;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerDetailCrawler {
    public void init() {
        List<String> startingPitchers = List.of(
                "원태인", "최승용", "소형준", "김광현", "박세웅",
                "네일", "임찬규", "폰세", "라일리", "로젠버그"
        );
        List<String> reliefPitchers = List.of(
                "배찬승", "이영하", "원상현", "이로운", "정철원",
                "조상우", "김진성", "박상원", "손주환", "김성민"
        );
        List<String> closingPitchers = List.of(
                "이호성", "김택연", "박영현", "조병현", "김원중",
                "정해영", "박명근", "김서현", "류진욱", "주승우"
        );

        List<String> catchers = List.of(
                "강민호", "양의지", "장성우", "조형우", "유강남", // 포수
                "김태군", "박동원", "최재훈", "김형준", "김재현"
        );

        // 1루수 (First Basemen)
        List<String> firstBasemen = List.of(
                "디아즈", "양석환", "문상철", "고명준", "나승엽",
                "위즈덤", "오스틴", "채은성", "데이비슨", "최주환"
        );

        // 2루수 (Second Basemen)
        List<String> secondBasemen = List.of(
                "류지혁", "강승호", "김상수", "정준재", "고승민",
                "김선빈", "신민재", "황영묵", "박민우", "오선진"
        );

        // 3루수 (Third Basemen)
        List<String> thirdBasemen = List.of(
                "김영웅", "임종성", "허경민", "최정", "손호영",
                "김도영", "문보경", "노시환", "서호철", "송성문"
        );

        // 유격수 (Shortstops)
        List<String> shortstops = List.of(
                "이재현", "오명진", "권동진", "박성한", "전민재",
                "박찬호", "오지환", "심우준", "김주원", "김태진"
        );

        // 외야수 (Outfielders)
        List<String> outfielders = List.of(
                "구자욱", "정수빈", "김민혁", "최준우", "레이예스",
                "나성범", "문성주", "최인호", "권희동", "카디네스",
                "김성윤", "케이브", "배정대", "최지훈", "윤동희",
                "최원준", "박해민", "플로리얼", "천재환", "임병욱",
                "김지찬", "김인태", "로하스", "에레디아", "장두성",
                "이우성", "송찬의", "이진영", "박건우", "이주형"
        );

        // 지명타자 (Designated Hitters)
        List<String> designatedHitters = List.of(
                "박병호", "김재환", "안현민", "한유섬", "전준우",
                "최형우", "김현수", "문현빈", "손아섭", "이형종"
        );


        //crawPitchers(startingPitchers, "STARTING_PITCHER");
        //crawPitchers(reliefPitchers, "MIDDLE_PITCHER");
        //crawPitchers(closingPitchers, "CLOSER_PITCHER");
        //crawFielders(firstBasemen, "FIRST_BASE");
        //crawFielders(secondBasemen, "SECOND_BASE");
        //crawFielders(thirdBasemen, "THIRD_BASE");
        //crawFielders(shortstops, "SHORTSTOP");
        //crawFielders(outfielders, "OUT_FIELD");
        crawFielders(designatedHitters, "DESIGNATED_HITTER");

//        crawCatchers(catchers);

    }
    public void crawPitchers(List<String> pitchers, String position) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            //투수 초기화
            for (String playerName : pitchers) {
                List<String> element = new ArrayList<>();
                // 1. 선수 이름 검색창에 입력
                element.add(playerName);

                // 포지션 드롭다운에서 '투수' 선택
                Select positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                positionDropdown.selectByVisibleText("투수");
                Thread.sleep(2000); // 검색창에서 처리 시간 대기

                WebElement searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                searchBox.clear();
                searchBox.sendKeys(playerName);
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

                //팀명 추가
                element.add(row.findElement(By.cssSelector("td:nth-child(1)")).getText());

                //포지션 추가
                element.add(position);

                //세부 능력치 가져오기
                String era = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // ERA
                String g = row.findElement(By.cssSelector("td:nth-child(3)")).getText();    // G

                //두번째 표
                row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"));
                String so = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // SO
                String whip = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // WHIP
                String avg = row.findElement(By.cssSelector("td:nth-child(12)")).getText(); // AVG
                String qs = row.findElement(By.cssSelector("td:nth-child(13)")).getText(); // QS

                Double score = calculateOverallScore(Double.valueOf(era),Double.valueOf(whip),Integer.valueOf(so),Integer.valueOf(g),Double.valueOf(avg),Integer.valueOf(qs));
                element.add(score.toString());
                //System.out.println("점수: " + score);
                //생년월일 추출
                WebElement birth = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_playerProfile_lblBirthday"));
                element.add(birth.getText());
                //System.out.println(birth.getText());
                //프로필 사진 추출
                WebElement imgElement = driver.findElement(By.id("cphContents_cphContents_cphContents_playerProfile_imgProgile"));
                String imgSrc = imgElement.getAttribute("src");
                element.add(imgSrc);

                makeString(element);
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

    public void crawCatchers(List<String> catchers) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (String playerName : catchers) {
                List<String> element = new ArrayList<>();
                element.add(playerName);

                // 포지션 드롭다운에서 포수 선택
                Select positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                positionDropdown.selectByVisibleText("포수");
                Thread.sleep(2000); // 검색창에서 처리 시간 대기

                WebElement searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                searchBox.clear();
                searchBox.sendKeys(playerName);
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
                WebElement row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.mb10 > table > tbody > tr"));

                //팀명 추가
                element.add(row.findElement(By.cssSelector("td:nth-child(1)")).getText());

                //포지션 추가
                element.add("CATCHER");

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

                element.add(score.toString());

                //생년월일 추출
                WebElement birth = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_playerProfile_lblBirthday"));
                element.add(birth.getText());

                //프로필 사진 추출
                WebElement imgElement = driver.findElement(By.id("cphContents_cphContents_cphContents_playerProfile_imgProgile"));
                String imgSrc = imgElement.getAttribute("src");
                element.add(imgSrc);

                makeString(element);
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

    public void crawFielders( List<String> players, String position) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            for (String playerName : players) {
                List<String> element = new ArrayList<>();
                element.add(playerName);

                // 포지션 드롭다운에서 포지션 선택
                Select positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                String pos = position.equals("OUT_FIELD") ? "외야수" : "내야수";
                positionDropdown.selectByVisibleText(pos);
                Thread.sleep(2000); // 검색창에서 처리 시간 대기

                WebElement searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                searchBox.clear();
                searchBox.sendKeys(playerName);
                Thread.sleep(1000); // 검색창에서 처리 시간 대기

                // 2. 검색 버튼 클릭
                WebElement searchButton = driver.findElement(By.id("cphContents_cphContents_cphContents_btnSearch"));
                searchButton.click();
                Thread.sleep(2000); // 검색 결과가 나오도록 대기

                //포지션 변경 등 이유로 검색되지 않을 경우
                WebElement tbody = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody"));
                List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                if (rows.isEmpty()) {
                    if (pos.equals("외야수")) {
                        // 내야수로 드롭다운 변경
                        positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                        positionDropdown.selectByVisibleText("내야수");
                        Thread.sleep(1000);
                    }
                    if (pos.equals("내야수")) {
                        // 외야수로 드롭다운 변경
                        positionDropdown = new Select(driver.findElement(By.id("cphContents_cphContents_cphContents_ddlPosition")));
                        positionDropdown.selectByVisibleText("외야수");
                        Thread.sleep(1000);
                    }

                    // 검색 다시 수행
                    searchBox = driver.findElement(By.id("cphContents_cphContents_cphContents_txtSearchPlayerName"));
                    searchBox.clear();
                    searchBox.sendKeys(playerName);
                    Thread.sleep(1000);

                    searchButton = driver.findElement(By.id("cphContents_cphContents_cphContents_btnSearch"));
                    searchButton.click();
                    Thread.sleep(2000);
                }

                // 3. 해당 선수의 링크 클릭하여 상세 페이지로 이동
                WebElement playerLink = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_udpRecord > div.inquiry > table > tbody > tr > td:nth-child(2) > a"));
                playerLink.click();
                Thread.sleep(2000); // 상세 페이지가 로드될 시간을 대기
                // 4. 상세 페이지에서 원하는 정보 가져오기

                //능력치 가져오기
                WebElement row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div.tbl-type02.mb10 > table > tbody > tr"));

                //팀명 추가
                element.add(row.findElement(By.cssSelector("td:nth-child(1)")).getText());

                //포지션 추가
                element.add(position);
                element.add("0");
                //생년월일 추출
                WebElement birth = driver.findElement(By.cssSelector("#cphContents_cphContents_cphContents_playerProfile_lblBirthday"));
                element.add(birth.getText());

                //프로필 사진 추출
                WebElement imgElement = driver.findElement(By.id("cphContents_cphContents_cphContents_playerProfile_imgProgile"));
                String imgSrc = imgElement.getAttribute("src");
                element.add(imgSrc);

                makeString(element);
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

    public double calculateOverallScore(double era, double whip, int so, int g, double avg, int qs) {
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


    private void makeString(List<String> element) {
        String result = String.join(", ", element);
        System.out.println(result);
    }
}
