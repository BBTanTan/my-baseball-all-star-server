package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.repository.PlayerChoiceCountRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void increasePlayerChoiceCount(Long playerId) {
        PlayerChoiceCount choiceCount = playerChoiceCountRepository.getById(playerId);
        choiceCount.increase();
    }

    @Transactional(readOnly = true)
    public List<Long> readChoiceCounts(List<Long> playerIds) {
        return playerChoiceCountRepository.findAllById(playerIds).stream()
                .map(PlayerChoiceCount::getCount)
                .toList();
    }
}
