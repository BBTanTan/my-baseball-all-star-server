package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AllStarFacadeService {

    private final PlayService playService;
    private final PlayerService playerService;
    private final PlayerChoiceCountService playerChoiceCountService;

    @Transactional
    public PlayResultResponse soloPlay(SoloPlayRequest request) {
        TeamRoaster home = makeTeamRoaster(request.homeTeam());
        TeamRoaster away = makeTeamRoaster(request.awayTeam());
        List<Integer> playResult = playService.soloPlay(home, away);
        return PlayResultResponse.of(home, away, playResult);
    }

    private TeamRoaster makeTeamRoaster(TeamRequest teamRequest) {
        playerChoiceCountService.increasePlayersChoiceCount(teamRequest.playerIds());
        List<Player> players = playerService.readPlayers(teamRequest.playerIds());
        List<Long> playerChoiceCounts = playerChoiceCountService.readChoiceCounts(teamRequest.playerIds());
        return new TeamRoaster(teamRequest.teamName(), players, playerChoiceCounts);
    }

    @Transactional
    public List<PlayerResponse> findAllPlayers() {
        return playerService.readAllPlayers();
    }
}
