package bbTan.my_baseball_all_star.domain;

import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TeamRoaster {

    private static final int TEAM_PLAYER_COUNT = 12;
    public static final int OUT_FIELD_VALID_COUNT = 3;
    public static final int POSITION_VALID_COUNT = 1;

    private String name;
    private List<Player> players;
    private List<Long> playerChoiceCounts;

    public TeamRoaster(String name, List<Player> players, List<Long> playerChoiceCounts) {
        this.name = name;
        validatePlayers(players);
        this.players = players;
        validatePlayerChoiceCounts(playerChoiceCounts);
        this.playerChoiceCounts = playerChoiceCounts;
    }

    private void validatePlayers(List<Player> players) {
        validatePlayerSize(players);
        validateNoDuplicatePlayers(players);
        validateAllPositions(players);
    }

    private void validatePlayerSize(List<Player> players) {
        if (players.size() != TEAM_PLAYER_COUNT) {
            throw new AllStarException(ExceptionCode.INVALID_TEAM_PLAYERS_SIZE);
        }
    }

    private void validateNoDuplicatePlayers(List<Player> players) {
        long uniqueCount = players.stream()
                .map(Player::getId)
                .distinct()
                .count();

        if (uniqueCount != players.size()) {
            throw new AllStarException(ExceptionCode.DUPLICATE_PLAYERS_IN_TEAM_ROSTER);
        }
    }

    private void validateAllPositions(List<Player> players) {
        Map<Position, Long> positionCountMap = players.stream()
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));
        boolean isAllPositionsValid = Arrays.stream(Position.values())
                .allMatch(position -> isValidCount(position, positionCountMap.getOrDefault(position, 0L)));
        if (!isAllPositionsValid) {
            throw new AllStarException(ExceptionCode.MISSING_POSITION_IN_TEAM_ROSTER);
        }
    }

    private boolean isValidCount(Position position, long count) {
        if (position == Position.OUT_FIELD) {
            return count == OUT_FIELD_VALID_COUNT;
        }
        return count == POSITION_VALID_COUNT;
    }

    private void validatePlayerChoiceCounts(List<Long> playerChoiceCounts) {
        if (playerChoiceCounts.size() != TEAM_PLAYER_COUNT) {
            throw new AllStarException(ExceptionCode.INVALID_CHOICE_COUNT_SIZE);
        }
    }

    public List<Double> readPlayerScores() {
        return players.stream()
                .map(Player::getScore)
                .toList();
    }

    public List<Long> readPlayerIds() {
        return players.stream()
                .map(Player::getId)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamRoaster that = (TeamRoaster) o;
        return Objects.equals(name, that.name) && Objects.equals(players, that.players)
                && Objects.equals(playerChoiceCounts, that.playerChoiceCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, players, playerChoiceCounts);
    }

    @Override
    public String toString() {
        return "TeamRoaster{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", playerChoiceCounts=" + playerChoiceCounts +
                '}';
    }
}
