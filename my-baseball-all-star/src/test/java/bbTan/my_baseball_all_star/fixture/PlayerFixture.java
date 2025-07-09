package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.Club;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerFixture {

    public static Player PLAYER1() {
        return new Player("올스타1", Club.SAMSUNG_LIONS, Position.CATCHER, LocalDate.now(), 64.35, "https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/69446.jpg" );
    }

    public static Player PLAYER2() {
        return new Player("올스타2", Club.KIA_TIGERS, Position.OUT_FIELD, LocalDate.now(), 64.35, "https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/69446.jpg");
    }

    public static Player PLAYER3() {
        return new Player("올스타3", Club.DOOSAN_BEARS, Position.CLOSER_PITCHER, LocalDate.now(), 64.35, "https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/69446.jpg");
    }

    public static List<Player> PLAYERS() {
        List<Player> players = new ArrayList<>();
        Club[] clubs = Club.values();
        int clubIndex = 0;
        long id = 1L;

        for (Position position : Position.values()) {
            int count = position == Position.OUT_FIELD ? 3 : 1;
            for (int i = 0; i < count; i++) {
                Player player = new Player(
                        "Player" + id,
                        clubs[clubIndex % clubs.length],
                        position,
                        LocalDate.of(1990, 1, 1).plusDays(id),
                        50.0 + id,
                        "https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/69446.jpg"
                );
                setId(player, id);
                players.add(player);
                id++;
                clubIndex++;
            }
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
