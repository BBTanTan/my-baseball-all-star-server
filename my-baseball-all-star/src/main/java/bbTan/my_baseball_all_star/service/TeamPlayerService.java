package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import bbTan.my_baseball_all_star.repository.TeamPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamPlayerService {

    private final TeamPlayerRepository teamPlayerRepository;

    @Transactional(readOnly = true)
    public List<Player> readByTeamId(Long teamId) {
        return teamPlayerRepository.findByTeamId(teamId).stream()
                .map(TeamPlayer::getPlayer)
                .toList();
    }
}
