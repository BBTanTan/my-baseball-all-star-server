package bbTan.my_baseball_all_star.controller;

import bbTan.my_baseball_all_star.global.utils.PlayerDetailCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final PlayerDetailCrawler playerDetailCrawler;

    @GetMapping("/test")
    public void test() {
        playerDetailCrawler.craw();
    }
}
