package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import java.util.Collections;
import java.util.List;

public class PlayerChoiceCountFixture {
    public static List<Long> PLAYER_CHOICE_COUNT_VALUES() {
        return Collections.nCopies(12, 0L);
    }

    public static List<Long> PLAYER_CHOICE_COUNT_VALUES_10() {
        return Collections.nCopies(10, 0L);
    }
}
