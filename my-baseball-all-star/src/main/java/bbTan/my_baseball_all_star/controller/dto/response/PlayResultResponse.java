package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.List;

public record PlayResultResponse(TeamResultResponse homeTeam,
                                 TeamResultResponse awayTeam
) {
    public static PlayResultResponse of(TeamRoaster home, TeamRoaster away, List<Integer> playResult) {
        TeamResultResponse homeTeam = new TeamResultResponse(home.getName(), playResult.get(0));
        TeamResultResponse awayTeam = new TeamResultResponse(away.getName(), playResult.get(1));
        return new PlayResultResponse(homeTeam, awayTeam);
    }
}
