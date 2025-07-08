package bbTan.my_baseball_all_star.global.utils;

import bbTan.my_baseball_all_star.domain.Player;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerDetailCrawler {
    public void  craw() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.koreabaseball.com/Player/Search.aspx");

            //정보가 필요한 선수 리스트
            List<String> pitchers = List.of(
                    "원태인", "최승용", "소형준", "김광현", "박세웅", // 선발
                    "네일", "임찬규", "폰세", "라일리", "로젠버그", "배찬승", "이영하", "원상현", "이로운", "정철원",
                    "조상우", "김진성", "박상원", "손주환", "김성민", // 중간
                    "이호성", "김택연", "박영현", "조병현", "김원중",
                    "정해영", "박명근", "김서현", "류진욱", "주승우" // 마무리
            );
            List<String> playerNames = List.of(
                    "강민호", "양의지", "장성우", "조형우", "유강남", // 포수
                    "김태군", "박동원", "최재훈", "김형준", "김재현",
                    "디아즈", "양석환", "문상철", "고명준", "나승엽", // 1루수
                    "위즈덤", "오스틴", "채은성", "데이비슨", "최주환",
                    "류지혁", "강승호", "김상수", "정준재", "고승민", // 2루수
                    "김선빈", "신민재", "황영묵", "박민우", "오선진",
                    "김영웅", "임종성", "허경민", "최정", "손호영", // 3루수
                    "김도영", "문보경", "노시환", "서호철", "송성문",
                    "이재현", "오명진", "권동진", "박성한", "전민재", // 유격수
                    "박찬호", "오지환", "심우준", "김주원", "김태진",
                    "구자욱", "정수빈", "김민혁", "최준우", "레이예스", // 외야수
                    "나성범", "문성주", "최인호", "권희동", "카디네스",
                    "김성윤", "케이브", "배정대", "최지훈", "윤동희",
                    "최원준", "박해민", "플로리얼", "천재환", "임병욱",
                    "김지찬", "김인태", "로하스", "에레디아", "장두성",
                    "이우성", "송찬의", "이진영", "박건우", "이주형",
                    "박병호", "김재환", "안현민", "한유섬", "전준우", // 지명타자
                    "최형우", "김현수", "문현빈", "손아섭", "이형종"
            );

            //투수 초기화
            for (String playerName : pitchers) {
                // 1. 선수 이름 검색창에 입력

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
                System.out.println(row.getText());

                String era = row.findElement(By.cssSelector("td:nth-child(2)")).getText();  // ERA
                String g = row.findElement(By.cssSelector("td:nth-child(3)")).getText();    // G

                //두번째 표
                row = driver.findElement(By.cssSelector("#contents > div.sub-content > div.player_records > div:nth-child(4) > table > tbody > tr"));
                String so = row.findElement(By.cssSelector("td:nth-child(5)")).getText(); // 두 번째 표의 SO
                String whip = row.findElement(By.cssSelector("td:nth-child(11)")).getText(); // WHIP
                String avg = row.findElement(By.cssSelector("td:nth-child(12)")).getText(); // AVG
                String qs = row.findElement(By.cssSelector("td:nth-child(13)")).getText(); // QS
                String result = era + " , " + g + " , " + so + " , "+ whip+ " , " + avg + " , "+ qs;
                System.out.println(result);

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
}
