package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.List;

public class TeamRoasterFixture {

    public static TeamRoaster VALID_ROSTER(String name) {
        List<Player> players = PlayerFixture.PLAYERS();
        List<Long> counts = PlayerChoiceCountFixture.PLAYER_CHOICE_COUNT_VALUES();
        return new TeamRoaster(name, players, counts);
    }
}

