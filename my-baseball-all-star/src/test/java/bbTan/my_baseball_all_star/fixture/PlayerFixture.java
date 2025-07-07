package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.Club;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerFixture {

    public static Player PLAYER() {
        return new Player("올스타", Club.SAMSUNG_LIONS, Position.CATCHER, LocalDate.now(), 64.35);
    }

    public static List<Player> PLAYERS() {
        Position[] positions = Position.values();
        Club[] clubs = Club.values();
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < positions.length; i++) {
            Player player = new Player(
                    "Player" + (i + 1),
                    clubs[i % clubs.length],
                    positions[i],
                    LocalDate.of(1990, 1, 1).plusDays(i),
                    50.0 + i
            );
            setId(player, (long) (i + 1));
            players.add(player);
        }

        return players;
    }

    private static void setId(Player player, Long id) {
        try {
            Field field = Player.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(player, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID on Player", e);
        }
    }

}
