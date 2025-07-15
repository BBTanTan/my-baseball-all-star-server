package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.fixture.PlayerChoiceCountFixture;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeamScoreCalculatorTest {

    @DisplayName("팀 점수 계산 성공")
    @Test
    void calculate() {
        // given
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();
        TeamRoaster team = new TeamRoaster("KBO 올스타", players, counts);

        // when
        Integer score = TeamScoreCalculator.calculate(team);

        // then
        assertThat(score).isBetween(0, 15);
    }
}
