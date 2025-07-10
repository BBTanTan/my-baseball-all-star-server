package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.PlayResult;

public record TeamPlayResultScoreResponse(String awayTeamName, int homeTeamScore, int awayTeamScore)
{
    public static TeamPlayResultScoreResponse of(PlayResult playResult) {
        return new TeamPlayResultScoreResponse(playResult.getAwayTeamName(), playResult.getHomeTeamScore(), playResult.getAwayTeamScore());
    }
}
