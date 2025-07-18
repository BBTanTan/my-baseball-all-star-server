package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamPlayResultRequest;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayCreateResponse;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayTeamResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PositionGroupResponse;
import bbTan.my_baseball_all_star.controller.dto.response.RandomTeamPlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.TeamPlayResultResponse;
import bbTan.my_baseball_all_star.domain.PlayResult;
import bbTan.my_baseball_all_star.domain.SelectMode;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AllStarFacadeService {

    private static final String RANDOM_TEAM = "RANDOM TEAM";

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
        TeamRoaster home = makeTeamRoasterByExistedTeam(homeTeam);
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

    @Transactional
    public RandomTeamPlayerResponse makeRandomTeamRoaster(String mode) {
        if (!SelectMode.isValidSelectMode(mode)) {
            throw new AllStarException(ExceptionCode.INVALID_REQUEST_PATH);
        }

        List<Player> selectedPlayers = playerService.randomPlayerSelection();
        List<Long> playerIds = selectedPlayers.stream().map(Player::getId).toList();
        List<Long> selectedPlayerChoiceCount = playerChoiceCountService.readChoiceCounts(playerIds);
        return RandomTeamPlayerResponse.fromEntity(new TeamRoaster(RANDOM_TEAM, selectedPlayers, selectedPlayerChoiceCount));
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
    public List<PositionGroupResponse> findAllPlayers() {
        return playerService.readAllPlayers().stream()
                .map(PlayerResponse::fromEntity)
                .collect(Collectors.groupingBy(PlayerResponse::position))
                .entrySet().stream()
                .map(entry -> new PositionGroupResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    private TeamRoaster makeTeamRoasterByExistedTeam(Team team) {
        List<Player> players = teamPlayerService.readByTeamId(team.getId());
        List<Long> playerIds = players.stream().map(Player::getId).toList();
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
        List<PlayerResponse> players = teamPlayerService.readByTeamId(team.getId())
                .stream().map(PlayerResponse::fromEntity).toList();
        return new FriendPlayTeamResponse(team.getId(), team.getName(), players);
    }

    @Transactional(readOnly = true)
    public TeamPlayResultResponse readTeamPlayResults(Long teamId, TeamPlayResultRequest request) {
        Team team = playShareService.readTeamByOwner(teamId, request.password());
        List<PlayResult> playResults = playResultService.readByTeam(team);
        return TeamPlayResultResponse.of(team, playResults);
    }
}
