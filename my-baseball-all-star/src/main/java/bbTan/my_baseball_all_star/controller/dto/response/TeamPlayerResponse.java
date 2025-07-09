package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.List;

public record TeamPlayerResponse(List<Player> players) {
    public static TeamPlayerResponse fromEntity (TeamRoaster teamRoaster) {
        return new TeamPlayerResponse(teamRoaster.getPlayers());
    }
}
