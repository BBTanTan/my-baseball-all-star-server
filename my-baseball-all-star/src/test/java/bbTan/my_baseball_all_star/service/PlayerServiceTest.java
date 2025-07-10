package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        List<Player> players = playerService.readAllPlayers();

        // then
        assertAll(
                () -> assertThat(players).hasSize(12),
                () -> assertThat(players).isNotEmpty()
        );
    }

    @DisplayName("랜덤으로 선수 추출")
    @Test
    void selectRandomPlayers() {
        //when
        List<Player> selectedPlayers = playerService.randomPlayerSelection();

        // then
        assertAll(
                () -> assertNotNull(selectedPlayers, "결과는 null이 아니어야 한다."),
                () -> {
                    Set<Position> positions = selectedPlayers.stream()
                            .map(Player::getPosition)
                            .collect(Collectors.toSet());
                    assertEquals(positions.size(), 10, "포지션이 중복되지 않아야 한다.");
                }
        );
    }

}
