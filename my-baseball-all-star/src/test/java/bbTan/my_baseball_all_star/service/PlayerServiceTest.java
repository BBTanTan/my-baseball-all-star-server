package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerServiceTest extends IntegrationTestSupport {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("ID 리스트에 해당하는 Player들 읽기 성공")
    void readPlayers_returnsCorrectPlayers() {
        // given
        Player p1 = playerRepository.save(PlayerFixture.PLAYER1());
        Player p2 = playerRepository.save(PlayerFixture.PLAYER2());
        Player p3 = playerRepository.save(PlayerFixture.PLAYER3());

        List<Long> ids = List.of(p1.getId(), p3.getId());

        // when
        List<Player> result = playerService.readPlayers(ids);

        // then
        assertThat(result).extracting(Player::getId)
                .containsExactlyInAnyOrder(p1.getId(), p3.getId());
    }

    @DisplayName("모든 선수 정보 조회")
    @Test
    void findAllPlayers() {
        // when
        List<PlayerResponse> players = playerService.readAllPlayers();

        // then
        assertThat(players).isNotEmpty();
        assertThat(players).hasSize(12); // 대략적인 개수 검증
    }


}
