package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public List<Player> readPlayers(List<Long> playerIds) {
        return playerRepository.findAllById(playerIds);
    }
}
