package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamPlayResultRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayCreateResponse;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayTeamResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PositionGroupResponse;
import bbTan.my_baseball_all_star.controller.dto.response.RandomTeamPlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.TeamPlayResultResponse;
import bbTan.my_baseball_all_star.domain.PlayShare;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.fixture.TeamRoasterFixture;
import bbTan.my_baseball_all_star.repository.PlayShareRepository;
import bbTan.my_baseball_all_star.repository.PlayerChoiceCountRepository;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AllStarFacadeServiceTest extends IntegrationTestSupport {

    @Autowired
    AllStarFacadeService allStarFacadeService;

    @Autowired
    private AllStarFacadeService facadeService;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerChoiceCountRepository playerChoiceCountRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayShareRepository playShareRepository;

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

    @DisplayName("친구 초대용 경기 생성 성공")
    @Test
    void createFriendPlay() {
        // given
        List<Long> playerIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);
        String teamName = "MyTeam";
        String password = "password";

        FriendPlayCreateRequest request = new FriendPlayCreateRequest(teamName, playerIds, password);

        // when
        FriendPlayCreateResponse response = facadeService.createFriendPlay(request);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.teamUuid()).isNotBlank()
        );
    }

    @DisplayName("친구 초대용 경기 팀 조회 성공")
    @Test
    void readFriendPlayTeam() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM1());
        PlayShare playShare = playShareRepository.save(new PlayShare(team, "1234"));
        String teamUrl = playShare.getUrl();

        // when
        FriendPlayTeamResponse response = facadeService.readFriendPlayTeam(teamUrl);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.teamId()).isEqualTo(team.getId()),
                () -> assertThat(response.teamName()).isEqualTo(team.getName())
        );
    }

    @DisplayName("팀 경기 결과 조회 성공")
    @Test
    void readTeamPlayResults() {
        // given
        String password = "pw1234";
        TeamPlayResultRequest request = new TeamPlayResultRequest(password);

        // when
        TeamPlayResultResponse response = facadeService.readTeamPlayResults(1L, request);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.teamName()).isEqualTo("TestTeam"),
                () -> assertThat(response.playResults()).hasSize(2),
                () -> assertThat(response.playResults())
                        .extracting("awayTeamName")
                        .containsExactlyInAnyOrder("Away1", "Away2")
        );
    }

    @Test
    @DisplayName("랜덤 팀 생성 성공")
    void makeRandomTeamRoaster() {
        // when
        RandomTeamPlayerResponse response = facadeService.makeRandomTeamRoaster("random");

        // then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(12,response.playerResponses().size())
        );
    }

    @Test
    @DisplayName("포지션 별로 선수 정보 반환 성공")
    void getAllPlayersByPosition() {
        // when
        List<PositionGroupResponse> grouped = facadeService.findAllPlayers();

        // then
        Map<String, List<PlayerResponse>> map = grouped.stream()
                .collect(Collectors.toMap(PositionGroupResponse::position, PositionGroupResponse::players));

        assertAll(
                () -> assertEquals(10, grouped.size()),
                () -> assertThat(map.get("선발 투수").size()).isGreaterThanOrEqualTo(1),
                () -> assertThat(map.get("외야수").size()).isGreaterThanOrEqualTo(3)
        );
    }
}
