package bbTan.my_baseball_all_star.global.utils;

import bbTan.my_baseball_all_star.domain.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebCrawler {
    private final String URL = "https://www.koreabaseball.com/Record/Player/HitterBasic/Basic1.aspx";

    public void crawl() throws IOException {
        Document doc = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0") // 일부 사이트는 user-agent 필수
                .timeout(5000)
                .get();

        List<Player> players = new ArrayList<>();
        Elements rows = doc.select("div.record_result table tbody tr");
        /*
        private String name;

    @Enumerated(EnumType.STRING)
    private Club club;

    @Enumerated(EnumType.STRING)
    private Position position;

    private LocalDate dateOfBirth;

    private Double score;
         */
        System.out.println("=== 가져온 HTML ===");
        System.out.println(doc.html());
        System.out.println("======");

        System.out.println("rows.size(): " + rows.size());

        for (Element row : rows) {
            Elements tds = row.select("td");
            for (Element td : tds) {
                System.out.print(td.text() + "\t");
            }
            System.out.println();
        }
    }
}
