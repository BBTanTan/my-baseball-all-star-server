package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.PlayShare;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import bbTan.my_baseball_all_star.repository.PlayShareRepository;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlayShareServiceTest extends IntegrationTestSupport {

    @Autowired
    private PlayShareService playShareService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayShareRepository playShareRepository;

    @DisplayName("공유 URL 생성 성공")
    @Test
    void createShareUrl() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM1());
        String password = "1234";

        // when
        String url = playShareService.createShareUrl(team, password);

        // then
        List<PlayShare> saved = playShareRepository.findAll();
        assertAll(
                () -> assertThat(saved).hasSize(1),
                () -> assertThat(saved.get(0).getTeam().getId()).isEqualTo(team.getId()),
                () -> assertThat(saved.get(0).getPassword()).isEqualTo(password),
                () -> assertThat(saved.get(0).getUrl()).isEqualTo(url)
        );
    }

    @DisplayName("URL로 팀 조회 성공")
    @Test
    void readTeamByUrl() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM1());
        PlayShare playShare = playShareRepository.save(new PlayShare(team, "password"));

        // when
        Team foundTeam = playShareService.readTeamByUrl(playShare.getUrl());

        // then
        assertAll(
                () -> assertThat(foundTeam.getId()).isEqualTo(team.getId()),
                () -> assertThat(foundTeam.getName()).isEqualTo(team.getName())
        );
    }

    @DisplayName("URL로 팀 조회 실패 : 존재하지 않는 URL인 경우")
    @Test
    void readTeamByUrl_notFound_exception() {
        // given
        String nonExistentUrl = "non-existent-url";

        // when & then
        assertThatThrownBy(() -> playShareService.readTeamByUrl(nonExistentUrl))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.TEAM_URL_NOT_FOUND.getMessage());
    }
}
