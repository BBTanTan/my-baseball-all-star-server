package bbTan.my_baseball_all_star.controller.dto.response;

import java.util.List;

public record FriendPlayTeamResponse(Long teamId, String teamName, List<PlayerResponse> players) {
}
