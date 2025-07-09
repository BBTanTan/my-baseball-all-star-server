package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.repository.PlayerChoiceCountRepository;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AllStarFacadeServiceTest extends IntegrationTestSupport {

    @Autowired
    AllStarFacadeService allStarFacadeService;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerChoiceCountRepository playerChoiceCountRepository;

    @Autowired
    private AllStarFacadeService facadeService;

    @Test
    @DisplayName("홀로 경기 성공")
    void soloPlay() {
        // given
        List<Long> playerIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);

        TeamRequest home = new TeamRequest("HomeTeam", playerIds);
        TeamRequest away = new TeamRequest("AwayTeam", playerIds);
        SoloPlayRequest request = new SoloPlayRequest(home, away);

        // when
        PlayResultResponse result = facadeService.soloPlay(request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.homeTeam()).isNotNull(),
                () -> assertThat(result.awayTeam()).isNotNull(),
                () -> assertThat(result.homeTeam().teamName()).isEqualTo("HomeTeam"),
                () -> assertThat(result.awayTeam().teamName()).isEqualTo("AwayTeam"),
                () -> assertThat(result.homeTeam().teamScore()).isBetween(0, 20),
                () -> assertThat(result.awayTeam().teamScore()).isBetween(0, 20)
        );
    }

    @Test
    @DisplayName("친구와 경기 성공")
    void friendPlay() {
        // given
        Long homeTeamId = 1L;
        List<Long> playerIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);
        TeamRequest awayTeamRequest = new TeamRequest("AwayTeam", playerIds);
        FriendPlayRequest request = new FriendPlayRequest(homeTeamId, awayTeamRequest);

        // when
        PlayResultResponse result = facadeService.friendPlay(request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.homeTeam()).isNotNull(),
                () -> assertThat(result.awayTeam()).isNotNull(),
                () -> assertThat(result.homeTeam().teamName()).isNotEmpty(),
                () -> assertThat(result.awayTeam().teamName()).isEqualTo("AwayTeam"),
                () -> assertThat(result.homeTeam().teamScore()).isBetween(0, 20),
                () -> assertThat(result.awayTeam().teamScore()).isBetween(0, 20)
        );
    }
}
