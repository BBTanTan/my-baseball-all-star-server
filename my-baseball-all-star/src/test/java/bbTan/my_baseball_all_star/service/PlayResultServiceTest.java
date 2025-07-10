package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.PlayResult;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.fixture.TeamRoasterFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import bbTan.my_baseball_all_star.repository.PlayResultRepository;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PlayResultServiceTest extends IntegrationTestSupport {

    @Autowired
    private PlayResultService playResultService;

    @Autowired
    private PlayResultRepository playResultRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("경기 결과 저장 성공")
    @Test
    void saveResult() {
        // given
        Team home = teamRepository.save(TeamFixture.TEAM1());
        TeamRoaster away = TeamRoasterFixture.VALID_ROSTER("팀2");

        List<Integer> scores = List.of(5, 3);

        // when
        playResultService.saveResult(home, away, scores);

        // then
        List<PlayResult> results = playResultRepository.findByTeamId(home.getId());
        PlayResult saved = results.get(0);
        assertAll(
                () -> assertThat(saved.getAwayTeamName()).isEqualTo(away.getName()),
                () -> assertThat(saved.getHomeTeamScore()).isEqualTo(scores.get(0)),
                () -> assertThat(saved.getAwayTeamScore()).isEqualTo(scores.get(1))
        );
    }

    @DisplayName("경기 결과 저장 실패 : 경기 결과 리스트 크기가 2가 아닐 경우")
    @Test
    void saveResult_invalidScoreSize_exception() {
        // given
        Team home = teamRepository.save(TeamFixture.TEAM1());
        TeamRoaster away = TeamRoasterFixture.VALID_ROSTER("팀2");

        List<Integer> invalidScores = List.of(5);

        // when & then
        assertThatThrownBy(() -> playResultService.saveResult(home, away, invalidScores))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.INVALID_RESULT_SCORE_SIZE.getMessage());
    }

    @DisplayName("팀으로 경기 결과 조회 성공")
    @Test
    void readByTeam() {
        // given
        Team home = teamRepository.save(TeamFixture.TEAM1());
        TeamRoaster away1 = TeamRoasterFixture.VALID_ROSTER("AwayTeam1");
        TeamRoaster away2 = TeamRoasterFixture.VALID_ROSTER("AwayTeam2");

        List<Integer> score1 = List.of(10, 8);
        List<Integer> score2 = List.of(5, 6);

        playResultService.saveResult(home, away1, score1);
        playResultService.saveResult(home, away2, score2);

        // when
        List<PlayResult> results = playResultService.readByTeam(home);

        // then
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results)
                        .extracting("awayTeamName")
                        .containsExactlyInAnyOrder("AwayTeam1", "AwayTeam2")
        );
    }

}
