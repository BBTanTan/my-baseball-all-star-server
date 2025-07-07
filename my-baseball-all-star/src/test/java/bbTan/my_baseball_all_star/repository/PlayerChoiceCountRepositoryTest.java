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

    @DisplayName("PlayerChoiceCount 갖고 오기 성공")
    @Test
    void getById() {
        // given
        Player player = playerRepository.save(PlayerFixture.PLAYER1());
        playerChoiceCountRepository.save(new PlayerChoiceCount(player));

        // when
        PlayerChoiceCount found = playerChoiceCountRepository.getById(player.getId());

        // then
        assertThat(found.getPlayer().getId()).isEqualTo(player.getId());
    }

    @DisplayName("PlayerChoiceCount 갖고 오기 실패: 존재하지 않음")
    @Test
    void getById_notFound_exception() {
        // expect
        assertThatThrownBy(() -> playerChoiceCountRepository.getById(999L))
                .isInstanceOf(AllStarException.class)
                .hasMessage(ExceptionCode.PLAYER_CHOICE_COUNT_NOT_FOUND.getMessage());
    }
}
