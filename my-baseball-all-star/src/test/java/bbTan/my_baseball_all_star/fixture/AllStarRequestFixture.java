package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamRequest;
import java.util.Collections;
import java.util.List;

public class AllStarRequestFixture {

    public static SoloPlayRequest SOLO_PLAY_REQUEST() {
        return new SoloPlayRequest(TEAM_REQUEST("home"), TEAM_REQUEST("away"));
    }

    public static SoloPlayRequest SOLO_PLAY_REQUEST_TEAM_NULL() {
        return new SoloPlayRequest(null, TEAM_REQUEST("away"));
    }

    public static SoloPlayRequest SOLO_PLAY_REQUEST_TEAM_NAME_EMPTY() {
        return new SoloPlayRequest(TEAM_REQUEST(""), TEAM_REQUEST("away"));
    }

    public static SoloPlayRequest SOLO_PLAY_REQUEST_PLAYERS_EMPTY() {
        return new SoloPlayRequest(TEAM_REQUEST_PLAYERS_EMPTY("home"), TEAM_REQUEST("away"));
    }

    public static TeamRequest TEAM_REQUEST(String teamName) {
        List<Long> playerIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);
        return new TeamRequest(teamName, playerIds);
    }

    public static TeamRequest TEAM_REQUEST_PLAYERS_EMPTY(String teamName) {
        List<Long> playerIds = List.of();
        return new TeamRequest(teamName, playerIds);
    }

    public static FriendPlayRequest FRIEND_PLAY_REQUEST() {
        return new FriendPlayRequest(1L, TEAM_REQUEST("away"));
    }

    public static FriendPlayRequest FRIEND_PLAY_REQUEST_HOME_ID_NULL() {
        return new FriendPlayRequest(null, TEAM_REQUEST("away"));
    }

    public static FriendPlayRequest FRIEND_PLAY_REQUEST_AWAY_TEAM_NAME_EMPTY() {
        return new FriendPlayRequest(1L, TEAM_REQUEST(null));
    }

    public static FriendPlayRequest FRIEND_PLAY_REQUEST_AWAY_PLAYERS_EMPTY() {
        return new FriendPlayRequest(1L, TEAM_REQUEST_PLAYERS_EMPTY("away"));
    }

    public static FriendPlayCreateRequest FRIEND_PLAY_CREATE_REQUEST() {
        return new FriendPlayCreateRequest("team", List.of(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L), "pw1234");
    }

    public static FriendPlayCreateRequest FRIEND_PLAY_CREATE_REQUEST_TEAM_NAME_NULL() {
        return new FriendPlayCreateRequest(null, List.of(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L), "pw1234");
    }

    public static FriendPlayCreateRequest FRIEND_PLAY_CREATE_REQUEST_PLAYERS_EMPTY() {
        return new FriendPlayCreateRequest("TeamA", Collections.emptyList(), "pw1234");
    }

    public static FriendPlayCreateRequest FRIEND_PLAY_CREATE_REQUEST_PASSWORD_EMPTY() {
        return new FriendPlayCreateRequest("TeamA", List.of(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L), "");
    }

}
