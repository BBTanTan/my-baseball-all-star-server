package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlayerChoiceCountRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PlayerChoiceCountRepository playerChoiceCountRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @DisplayName("Player Id로 PlayerChoiceCount 갖고 오기 성공")
    @Test
    void getByPlayerId() {
        // given
        Player player = playerRepository.save(PlayerFixture.PLAYER1());
        playerChoiceCountRepository.save(new PlayerChoiceCount(player));

        // when
        PlayerChoiceCount found = playerChoiceCountRepository.getByPlayerId(player.getId());

        // then
        assertThat(found.getPlayer().getId()).isEqualTo(player.getId());
    }

    @DisplayName("Player Id로 PlayerChoiceCount 갖고 오기 실패: 존재하지 않음")
    @Test
    void getByPlayerId_notFound_exception() {
        // expect
        assertThatThrownBy(() -> playerChoiceCountRepository.getByPlayerId(999L))
                .isInstanceOf(AllStarException.class)
                .hasMessage(ExceptionCode.PLAYER_CHOICE_COUNT_NOT_FOUND.getMessage());
    }
}
