package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import bbTan.my_baseball_all_star.repository.TeamPlayerRepository;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamPlayerServiceTest extends IntegrationTestSupport {

    @Autowired
    private TeamPlayerService teamPlayerService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @DisplayName("팀 ID로 소속된 선수들을 조회 성공")
    @Test
    void readByTeamId() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM1());
        Player player1 = playerRepository.save(PlayerFixture.PLAYER1());
        Player player2 = playerRepository.save(PlayerFixture.PLAYER2());

        teamPlayerRepository.save(new TeamPlayer(team, player1));
        teamPlayerRepository.save(new TeamPlayer(team, player2));

        // when
        List<Player> players = teamPlayerService.readByTeamId(team.getId());

        // then
        assertThat(players).hasSize(2);
    }

    @DisplayName("팀 ID로 소속된 선수들을 조회 성공 : 비었을 경우")
    @Test
    void readByTeamId_empty() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM2());

        // when
        List<Player> players = teamPlayerService.readByTeamId(team.getId());

        // then
        assertThat(players).isEmpty();
    }

    @DisplayName("팀과 선수 리스트로 TeamPlayer들을 생성 성공")
    @Test
    void createAll() {
        // given
        Team team = teamRepository.save(TeamFixture.TEAM1());
        Player player1 = playerRepository.save(PlayerFixture.PLAYER1());
        Player player2 = playerRepository.save(PlayerFixture.PLAYER2());
        List<Player> players = List.of(player1, player2);

        // when
        teamPlayerService.createAll(team, players);

        // then
        List<TeamPlayer> teamPlayers = teamPlayerRepository.findByTeamId(team.getId());
        assertThat(teamPlayers).hasSize(2);
    }
}
