package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AllStarFacadeService {

    private final PlayerService playerService;
    private final PlayerChoiceCountService playerChoiceCountService;
    private final TeamService teamService;
    private final TeamPlayerService teamPlayerService;
    private final PlayResultService playResultService;

    @Transactional
    public PlayResultResponse soloPlay(SoloPlayRequest request) {
        TeamRoaster home = makeTeamRoaster(request.homeTeam());
        TeamRoaster away = makeTeamRoaster(request.awayTeam());
        List<Integer> playResult = play(home, away);
        return PlayResultResponse.of(home, away, playResult);
    }

    @Transactional
    public PlayResultResponse friendPlay(FriendPlayRequest request) {
        Team homeTeam = teamService.readById(request.homeTeamId());
        TeamRoaster home = makeTeamRoaster(homeTeam);
        TeamRoaster away = makeTeamRoaster(request.awayTeam());
        List<Integer> playResult = play(home, away);
        playResultService.saveResult(homeTeam, away, playResult);
        teamService.recordMatchResult(request.homeTeamId(), isWin(playResult));
        return PlayResultResponse.of(home, away, playResult);
    }

    private boolean isWin(List<Integer> playResult) {
        int homeScore = playResult.get(0);
        int awayScore = playResult.get(1);
        return homeScore > awayScore;
    }

    private TeamRoaster makeTeamRoaster(TeamRequest teamRequest) {
        playerChoiceCountService.increasePlayersChoiceCount(teamRequest.playerIds());
        List<Player> players = playerService.readPlayers(teamRequest.playerIds());
        List<Long> playerChoiceCounts = playerChoiceCountService.readChoiceCounts(teamRequest.playerIds());
        return new TeamRoaster(teamRequest.teamName(), players, playerChoiceCounts);
    }

    private TeamRoaster makeTeamRoaster(Team team) {
        List<Player> players = teamPlayerService.readByTeamId(team.getId());
        List<Long> playerIds = players.stream().map(Player::getId).toList();
        playerChoiceCountService.increasePlayersChoiceCount(playerIds);
        List<Long> playerChoiceCounts = playerChoiceCountService.readChoiceCounts(playerIds);
        return new TeamRoaster(team.getName(), players, playerChoiceCounts);
    }

    public List<Integer> play(TeamRoaster home, TeamRoaster away) {
        Integer homeTeamScore = TeamScoreCalculator.calculate(home);
        Integer awayTeamScore = TeamScoreCalculator.calculate(away);
        return List.of(homeTeamScore, awayTeamScore);
    }
}
