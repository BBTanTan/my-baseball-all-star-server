package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayCreateResponse;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayTeamResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.TeamPlayerResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.ArrayList;
import java.util.Map;
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
    private final PlayShareService playShareService;

    @Transactional
    public PlayResultResponse soloPlay(SoloPlayRequest request) {
        TeamRoaster home = makeTeamRoaster(request.homeTeam().teamName(), request.homeTeam().playerIds());
        TeamRoaster away = makeTeamRoaster(request.awayTeam().teamName(), request.awayTeam().playerIds());
        List<Integer> playResult = play(home, away);
        return PlayResultResponse.of(home, away, playResult);
    }

    @Transactional
    public PlayResultResponse friendPlay(FriendPlayRequest request) {
        Team homeTeam = teamService.readById(request.homeTeamId());
        TeamRoaster home = makeTeamRoaster(homeTeam);
        TeamRoaster away = makeTeamRoaster(request.awayTeam().teamName(), request.awayTeam().playerIds());
        List<Integer> playResult = play(home, away);
        playResultService.saveResult(homeTeam, away, playResult);
        teamService.recordMatchResult(request.homeTeamId(), isWin(playResult));
        return PlayResultResponse.of(home, away, playResult);
    }

    @Transactional
    public FriendPlayCreateResponse createFriendPlay(FriendPlayCreateRequest request) {
        TeamRoaster home = makeTeamRoaster(request.teamName(), request.playerIds());
        Team homeTeam = teamService.create(home);
        teamPlayerService.createAll(homeTeam, home.getPlayers());
        String teamUuid = playShareService.createShareUrl(homeTeam, request.password());
        return new FriendPlayCreateResponse(teamUuid);
    }

    public TeamPlayerResponse getRandomTeamRoaster() {
        List<Player> selectedPlayers = playerService.randomPlayerSelection();
        List<Long> playerIds = selectedPlayers.stream().map(Player::getId).toList();


        List<Long> selectedPlayerChoiceCount = playerChoiceCountService.readChoiceCounts(playerIds);

        return TeamPlayerResponse.fromEntity(new TeamRoaster("RANDOM TEAM", selectedPlayers, selectedPlayerChoiceCount));
    }

    private boolean isWin(List<Integer> playResult) {
        int homeScore = playResult.get(0);
        int awayScore = playResult.get(1);
        return homeScore > awayScore;
    }

    private TeamRoaster makeTeamRoaster(String teamName, List<Long> playerIds) {
        playerChoiceCountService.increasePlayersChoiceCount(playerIds);
        List<Player> players = playerService.readPlayers(playerIds);
        List<Long> playerChoiceCounts = playerChoiceCountService.readChoiceCounts(playerIds);
        return new TeamRoaster(teamName, players, playerChoiceCounts);
    }

    @Transactional
    public List<PlayerResponse> findAllPlayers() {
        return playerService.readAllPlayers(); // TODO: DTO를 파사드 서비스가 만들도록 수정
    }

    private TeamRoaster makeTeamRoaster(Team team) {
        List<Player> players = teamPlayerService.readByTeamId(team.getId());
        List<Long> playerIds = players.stream().map(Player::getId).toList();
        playerChoiceCountService.increasePlayersChoiceCount(playerIds);
        List<Long> playerChoiceCounts = playerChoiceCountService.readChoiceCounts(playerIds);
        return new TeamRoaster(team.getName(), players, playerChoiceCounts);
    }

    private List<Integer> play(TeamRoaster home, TeamRoaster away) {
        Integer homeTeamScore = TeamScoreCalculator.calculate(home);
        Integer awayTeamScore = TeamScoreCalculator.calculate(away);
        return List.of(homeTeamScore, awayTeamScore);
    }

    @Transactional(readOnly = true)
    public FriendPlayTeamResponse readFriendPlayTeam(String teamUrl) {
        Team team = playShareService.readTeamByUrl(teamUrl);
        return new FriendPlayTeamResponse(team.getId(), team.getName());
    }
}
