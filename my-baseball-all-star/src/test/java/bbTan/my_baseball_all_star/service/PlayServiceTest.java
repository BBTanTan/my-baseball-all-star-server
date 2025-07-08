package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.fixture.TeamRoasterFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlayServiceTest extends IntegrationTestSupport {

    @Autowired
    private PlayService playService;

    @DisplayName("혼자 경기 성공")
    @Test
    void soloPlay() {
        // given
        TeamRoaster homeTeam = TeamRoasterFixture.VALID_ROSTER("Home");
        TeamRoaster awayTeam = TeamRoasterFixture.VALID_ROSTER("Away");

        // when
        List<Integer> scores = playService.soloPlay(homeTeam, awayTeam);

        // then
        assertAll(
                () -> assertThat(scores).hasSize(2),
                () -> assertThat(scores.get(0)).isBetween(0, 20),
                () -> assertThat(scores.get(1)).isBetween(0, 20)
        );
    }

}
