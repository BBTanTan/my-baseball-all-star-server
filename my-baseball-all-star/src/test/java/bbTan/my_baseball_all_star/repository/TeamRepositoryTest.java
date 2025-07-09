package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TeamRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("팀 갖고 오기 성공")
    @Test
    void getById() {
        // given
        Team saved = teamRepository.save(TeamFixture.TEAM1());

        // when
        Team result = teamRepository.getById(saved.getId());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(saved.getId()),
                () -> assertThat(result.getName()).isEqualTo(TeamFixture.TEAM1().getName())
        );
    }

    @DisplayName("팀 갖고 오기 실패 : 존재하지 않는 경우")
    @Test
    void getById_notFound_exception() {
        // given
        Long nonexistentId = 999L;

        // when & then
        assertThatThrownBy(() -> teamRepository.getById(nonexistentId))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.TEAM_NOT_FOUND.getMessage());
    }

    @DisplayName("전체 경기 횟수와 이긴 횟수 증가 성공")
    @Test
    void incrementTotalAndWinCount() {
        // given
        Team saved = teamRepository.save(TeamFixture.TEAM1());
        Long id = saved.getId();

        // when
        teamRepository.incrementTotalAndWinCount(id);
        Team updated = teamRepository.getById(id);

        // then
        assertAll(
                () -> assertThat(updated.getTotalPlayCount()).isEqualTo(1),
                () -> assertThat(updated.getWinPlayCount()).isEqualTo(1)
        );
    }

    @DisplayName("전체 경기 횟수 증가 성공")
    @Test
    void incrementTotalCount() {
        // given
        Team saved = teamRepository.save(new Team("teamC"));
        Long id = saved.getId();

        // when
        teamRepository.incrementTotalCount(id);
        Team updated = teamRepository.getById(id);

        // then
        assertAll(
                () -> assertThat(updated.getTotalPlayCount()).isEqualTo(1),
                () -> assertThat(updated.getWinPlayCount()).isEqualTo(0)
        );
    }
}
