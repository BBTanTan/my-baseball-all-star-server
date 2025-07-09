package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TeamServiceTest extends IntegrationTestSupport {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("팀 ID로 팀 조회 가능")
    @Test
    void readById() {
        // given
        Team saved = teamRepository.save(TeamFixture.TEAM1());

        // when
        Team result = teamService.readById(saved.getId());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(saved.getId()),
                () -> assertThat(result.getName()).isEqualTo(saved.getName())
        );
    }

    @DisplayName("경기 기록 반영 성공: 이겼을 경우")
    @Test
    void recordMatchResult_win() {
        // given
        Team saved = teamRepository.save(TeamFixture.TEAM1());

        // when
        teamService.recordMatchResult(saved.getId(), true);
        Team updated = teamRepository.getById(saved.getId());

        // then
        assertAll(
                () -> assertThat(updated.getTotalPlayCount()).isEqualTo(1),
                () -> assertThat(updated.getWinPlayCount()).isEqualTo(1)
        );
    }

    @DisplayName("경기 기록 반영 성공: 이기지 않았을 경우")
    @Test
    void recordMatchResult_notWin() {
        // given
        Team saved = teamRepository.save(TeamFixture.TEAM1());

        // when
        teamService.recordMatchResult(saved.getId(), false);
        Team updated = teamRepository.getById(saved.getId());

        // then
        assertAll(
                () -> assertThat(updated.getTotalPlayCount()).isEqualTo(1),
                () -> assertThat(updated.getWinPlayCount()).isEqualTo(0)
        );
    }
}
