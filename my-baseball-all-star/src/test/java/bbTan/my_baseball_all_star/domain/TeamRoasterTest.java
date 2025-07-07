package bbTan.my_baseball_all_star.domain;

import bbTan.my_baseball_all_star.fixture.PlayerChoiceCountFixture;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TeamRoasterTest {

    @DisplayName("TeamRoaster 생성 성공")
    @Test
    void teamRoasterConstructor() {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();

        TeamRoaster roster = new TeamRoaster("team", players, counts);

        assertAll(
                () -> assertThat(roster.getName()).isEqualTo("team"),
                () -> assertThat(roster.getPlayers()).containsExactlyElementsOf(players),
                () -> assertThat(roster.getPlayerChoiceCounts()).containsExactlyElementsOf(counts)
        );
    }

    @DisplayName("TeamRoster 생성 실패 : 선수단이 12명이 아닐 경우")
    @Test
    void teamRoasterConstructor_invalidPlayersSize_exception() {
        List<Player> players = PlayerFixture.PLAYERS().subList(0, 10);
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();

        assertThatThrownBy(() -> new TeamRoaster("Team", players, counts))
                .isInstanceOf(AllStarException.class)
                .hasMessage(ExceptionCode.INVALID_TEAM_PLAYERS_SIZE.getMessage());
    }

    @DisplayName("TeamRoster 생성 실패 : 중복 선수 있을 경우")
    @Test
    void teamRoasterConstructor_duplicatePlayer_exception() {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();
        players.set(0, PlayerFixture.PLAYER());
        players.set(1, PlayerFixture.PLAYER());

        assertThatThrownBy(() -> new TeamRoaster("test", players, counts))
                .isInstanceOf(AllStarException.class)
                .hasMessage(ExceptionCode.DUPLICATE_PLAYERS_IN_TEAM_ROSTER.getMessage());
    }

    @DisplayName("TeamRoster 생성 실패 : 중복 포지션이 있을 경우")
    @Test
    void teamRoasterConstructor_missingPositions_exception() {
        List<Player> players = PlayerFixture.PLAYERS();
        players.set(0, PlayerFixture.PLAYER());

        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();

        assertThatThrownBy(() -> new TeamRoaster("Team", players, counts))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.MISSING_POSITION_IN_TEAM_ROSTER.getMessage());
    }

    @DisplayName("TeamRoster 생성 실패 : player choice count 크기가 12가 아닐 경우")
    @Test
    void createTeamRoaster_invalidChoiceCountsSize_throws() {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES_10();

        assertThatThrownBy(() -> new TeamRoaster("Team", players, counts))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.INVALID_CHOICE_COUNT_SIZE.getMessage());
    }

    @DisplayName("선수들 점수 읽기 성공")
    @Test
    void readPlayerScores() {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();
        TeamRoaster roster = new TeamRoaster("Team", players, counts);

        List<Double> scores = roster.readPlayerScores();

        assertThat(scores).containsExactlyElementsOf(players.stream()
                .map(Player::getScore)
                .collect(Collectors.toList()));
    }

    @DisplayName("선수들 ID 읽기 성공")
    @Test
    void readPlayerIds() {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();
        TeamRoaster roster = new TeamRoaster("Team", players, counts);

        List<Long> ids = roster.readPlayerIds();

        assertThat(ids).containsExactlyElementsOf(players.stream()
                .map(Player::getId)
                .collect(Collectors.toList()));
    }
}
