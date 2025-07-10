package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.List;

public record RandomTeamPlayerResponse(List<PlayerResponse> playerResponses) {
    public static RandomTeamPlayerResponse fromEntity (TeamRoaster teamRoaster) {
        return new RandomTeamPlayerResponse(teamRoaster.getPlayers()
                .stream().map(PlayerResponse::fromEntity)
                .toList());
    }
}
