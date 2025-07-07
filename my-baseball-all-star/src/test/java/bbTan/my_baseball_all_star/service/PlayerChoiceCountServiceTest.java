package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.IntegrationTestSupport;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.fixture.PlayerFixture;
import bbTan.my_baseball_all_star.repository.PlayerChoiceCountRepository;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerChoiceCountServiceTest extends IntegrationTestSupport {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerChoiceCountRepository choiceCountRepository;

    @Autowired
    private PlayerChoiceCountService choiceCountService;

    private Player savedPlayer;

    @BeforeEach
    void setUp() {
        savedPlayer = playerRepository.save(PlayerFixture.PLAYER1());
        choiceCountRepository.save(new PlayerChoiceCount(savedPlayer));
    }

    @Test
    @DisplayName("선수 ID 리스트로 선택 수 증가 성공")
    void increasePlayersChoiceCount() {
        // given
        List<Long> ids = List.of(savedPlayer.getId());

        // when
        choiceCountService.increasePlayersChoiceCount(ids);

        // then
        PlayerChoiceCount updated = choiceCountRepository.getById(savedPlayer.getId());
        assertThat(updated.getCount()).isEqualTo(1L);
    }

    @DisplayName("선수 ID로 선택 수 증가 성공")
    @Test
    void increasePlayerChoiceCounts() {
        // given
        Long playerId = savedPlayer.getId();

        // when
        choiceCountService.increasePlayerChoiceCount(playerId);

        // then
        PlayerChoiceCount updated = choiceCountRepository.getById(playerId);
        assertThat(updated.getCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("선수 ID 리스트로 선택 수 조회 성공")
    void readChoiceCounts_success() {
        // given
        choiceCountService.increasePlayerChoiceCount(savedPlayer.getId());
        choiceCountService.increasePlayerChoiceCount(savedPlayer.getId());
        List<Long> ids = List.of(savedPlayer.getId());

        // when
        List<Long> counts = choiceCountService.readChoiceCounts(ids);

        // then
        assertThat(counts).containsExactly(2L);
    }

    @Test
    @DisplayName("선수 ID로 선택 수 증가 성공 : 동시에 여러 요청이 들어와도 정확하게 증가한다")
    void concurrent_increasePlayerChoiceCount() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long playerId = savedPlayer.getId();

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                boolean success = false;
                while (!success) {
                    try {
                        choiceCountService.increasePlayerChoiceCount(playerId);
                        success = true;
                    } catch (ObjectOptimisticLockingFailureException e) {
                        // 낙관적 락 충돌 발생 시 재시도
                    }
                }
                latch.countDown();
            });
        }

        latch.await();

        // then
        PlayerChoiceCount updated = choiceCountRepository.getById(playerId);
        assertThat(updated.getCount()).isEqualTo((long) threadCount);
    }

}
