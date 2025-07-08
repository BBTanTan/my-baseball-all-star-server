package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import bbTan.my_baseball_all_star.repository.TeamPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void createAll(Team team, List<Player> players) {
        List<TeamPlayer> teamPlayers = players.stream()
                .map(player -> new TeamPlayer(team, player))
                .toList();

        teamPlayerRepository.saveAll(teamPlayers);
    }

}
