package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.PlayResult;
import bbTan.my_baseball_all_star.domain.Team;
import java.util.List;

public record TeamPlayResultResponse(String teamName, Long totalPlayCount, Long winPlayCount,
                                     List<TeamPlayResultScoreResponse> playResults) {
    public static TeamPlayResultResponse of(Team team, List<PlayResult> playResults) {
        List<TeamPlayResultScoreResponse> playResultScores = playResults.stream()
                .map(TeamPlayResultScoreResponse::of)
                .toList();
        return new TeamPlayResultResponse(team.getName(), team.getTotalPlayCount(), team.getWinPlayCount(), playResultScores);
    }
}
