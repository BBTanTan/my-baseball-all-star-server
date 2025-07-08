package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.repository.PlayerChoiceCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerChoiceCountService {

    private final PlayerChoiceCountRepository playerChoiceCountRepository;

    @Transactional
    public void increasePlayersChoiceCount(List<Long> playerIds) {
        playerIds.forEach(this::increasePlayerChoiceCount);
    }

    @Retryable(
            value = { ObjectOptimisticLockingFailureException.class },
            maxAttempts = 10,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void increasePlayerChoiceCount(Long playerId) {
        PlayerChoiceCount choiceCount = playerChoiceCountRepository.getByPlayerId(playerId);
        choiceCount.increase();
    }

    @Transactional(readOnly = true)
    public List<Long> readChoiceCounts(List<Long> playerIds) {
        return playerChoiceCountRepository.findAllById(playerIds).stream()
                .map(PlayerChoiceCount::getCount)
                .toList();
    }
}
