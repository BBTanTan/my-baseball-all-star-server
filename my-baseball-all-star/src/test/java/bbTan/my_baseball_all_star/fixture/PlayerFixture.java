package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.Club;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlayerFixture {

    public static Player PLAYER() {
        return new Player("올스타", Club.SAMSUNG_LIONS, Position.CATCHER, LocalDate.now(), 64.35);
    }
}
